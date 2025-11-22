package com.diberardino.serversbrowserservice.model;


public record ServerInfo(
        String name,
        String ip,
        int port,
        int players,
        int ping,
        boolean dedicatedServer
) {
    public ServerInfo {
        if (ip == null || ip.isBlank()) {
            throw new IllegalArgumentException("IP cannot be null or blank");
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ServerInfo other &&
                this.ip.equals(other.ip) &&
                this.port == other.port;
    }

    @Override
    public int hashCode() {
        return ip.hashCode() + port;
    }
}