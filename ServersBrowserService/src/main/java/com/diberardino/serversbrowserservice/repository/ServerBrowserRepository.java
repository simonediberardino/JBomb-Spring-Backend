package com.diberardino.serversbrowserservice.repository;

import com.diberardino.serversbrowserservice.model.ServerInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public class ServerBrowserRepository {
    // Base key prefix for all server entries in Redis.
    // We use a consistent prefix so all server keys can be easily queried and managed.
    private static final String KEY = "server:";

    // Spring's RedisTemplate provides a high-level abstraction for interacting with Redis.
    // Using Redis here is intentional because:
    // 1. Server information is ephemeral (short-lived) and frequently updated.
    // 2. Redis is an in-memory key-value store, providing very fast reads/writes.
    // 3. Redis supports automatic expiration (TTL) for keys, simplifying cleanup of inactive servers.
    private final RedisTemplate<String, ServerInfo> redisTemplate;

    // Time-to-live (TTL) for server entries in Redis.
    // Any server not updated within 60 seconds will automatically expire.
    // This prevents stale data from lingering in Redis.
    private static final Duration TTL = Duration.ofSeconds(60);

    // Constructor injection for RedisTemplate.
    // Ensures that Spring provides the configured RedisTemplate bean to interact with Redis.
    public ServerBrowserRepository(RedisTemplate<String, ServerInfo> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Generate a unique Redis key for a given server.
     * Using "server:IP:PORT" format ensures that each server has its own distinct entry.
     * This key structure makes it easy to query, update, or delete servers individually.
     */
    private String key(ServerInfo server) {
        return "%s%s:%d".formatted(KEY, server.ip(), server.port());
    }

    /**
     * Retrieve all active servers from Redis.
     *
     * How it works:
     * 1. Fetch all keys matching the "server:" prefix.
     * 2. Use multiGet to fetch all corresponding ServerInfo objects efficiently.
     * 3. Return an empty list if no servers are found.
     *
     * Why Redis is ideal here:
     * - Ephemeral storage: Server info is temporary and may disappear quickly.
     * - High performance: Key-based lookups and multi-get are fast.
     * - TTL: Automatically removes servers that have not sent a heartbeat, no manual cleanup needed.
     */
    public Collection<ServerInfo> findAll() {
        Set<String> keys = redisTemplate.keys("%s*".formatted(KEY));

        if (keys == null || keys.isEmpty()) {
            return List.of(); // Return empty list if no servers found
        }

        List<ServerInfo> servers = redisTemplate.opsForValue().multiGet(keys);
        return servers == null ? List.of() : servers; // Ensure null-safety
    }

    /**
     * Save or update a server in Redis with a TTL.
     *
     * How it works:
     * 1. Compute a unique key for the server.
     * 2. Store the ServerInfo object in Redis with an expiration time.
     *
     * Why TTL is useful:
     * - Automatically removes inactive servers.
     * - Keeps Redis memory usage optimized.
     * - Reduces the need for additional cleanup logic.
     */
    public void save(ServerInfo server) {
        String key = key(server);
        redisTemplate.opsForValue().set(key, server, TTL);
    }

    /**
     * Remove a server from Redis explicitly.
     *
     * Use case:
     * - If a server shuts down gracefully, we can remove it immediately rather than waiting for TTL to expire.
     */
    public boolean remove(ServerInfo server) {
        return Boolean.TRUE.equals(redisTemplate.delete(key(server)));
    }
}
