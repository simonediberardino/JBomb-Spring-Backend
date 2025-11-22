package com.diberardino.serversbrowserservice.controller;

import com.diberardino.serversbrowserservice.model.ServerCreationResult;
import com.diberardino.serversbrowserservice.model.ServerInfo;
import com.diberardino.serversbrowserservice.service.ServerBrowserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Active servers periodically send a "heartbeat" to the backend to indicate they are online.
 * The backend stores this information in Redis (an in-memory database) for fast access.
 * When a client requests the list of servers, the controller fetches the data from Redis
 * through ServerBrowserService and returns it.
 */
@RestController
@RequestMapping("/servers") // Base URL path for all endpoints related to servers
public class ServerBrowserController {

    // Service layer that handles server-related logic.
    // The controller delegates all operations to this service.
    private final ServerBrowserService serverBrowserService;

    // Constructor injection ensures ServerBrowserService is provided by Spring.
    public ServerBrowserController(ServerBrowserService serverBrowserService) {
        this.serverBrowserService = serverBrowserService;
    }

    /**
     * Endpoint: GET /servers
     * <p>
     * This method is used by the frontend website or the JBomb game client
     * to retrieve the list of currently active game servers.
     * <p>
     * Users can see all active servers in the website UI or directly in the game server browser.
     */
    @GetMapping
    public Collection<ServerInfo> getAllServers() {
        return serverBrowserService.getAllServers();
    }

    /**
     * Endpoint: POST /servers
     * <p>
     * This method allows a game server to register itself with the backend.
     * - The server sends its information in the request body (IP, port, etc.).
     * - The backend adds the server to Redis with a short time-to-live (TTL).
     * This TTL ensures that inactive servers are automatically removed if they stop sending heartbeats.
     * <p>
     * Response:
     * - If the server is newly created, returns HTTP 201 (Created) with the server info.
     * - If the server already exists, returns HTTP 200 (OK) with the server info.
     * <p>
     * Redis ensures quick read/write operations for server info, keeping the server browser responsive.
     */
    @PostMapping
    public ResponseEntity<ServerInfo> addServer(@RequestBody ServerInfo serverInfo) {
        ServerCreationResult serverCreationResult = serverBrowserService.createServer(serverInfo);

        if (serverCreationResult.created()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(serverCreationResult.serverInfo());
        } else {
            return ResponseEntity.ok(serverCreationResult.serverInfo());
        }
    }
}
