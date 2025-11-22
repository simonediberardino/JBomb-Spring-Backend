package com.diberardino.serversbrowserservice.service;

import com.diberardino.serversbrowserservice.model.ServerCreationResult;
import com.diberardino.serversbrowserservice.model.ServerInfo;
import com.diberardino.serversbrowserservice.repository.ServerBrowserRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ServerBrowserService {
    private final ServerBrowserRepository serverBrowserRepository;

    public ServerBrowserService(ServerBrowserRepository serverBrowserRepository) {
        this.serverBrowserRepository = serverBrowserRepository;
    }

    public Collection<ServerInfo> getAllServers() {
        return this.serverBrowserRepository.findAll();
    }

    public ServerCreationResult createServer(ServerInfo serverInfo) {
        boolean created = this.serverBrowserRepository.remove(serverInfo);
        this.serverBrowserRepository.save(serverInfo);
        return new ServerCreationResult(serverInfo, created);
    }
}
