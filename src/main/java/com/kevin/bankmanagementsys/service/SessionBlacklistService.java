package com.kevin.bankmanagementsys.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SessionBlacklistService {
    private final Set<String> sessionBlackList = new HashSet<>();

    public void addToBlackList(String sessionId) {
        sessionBlackList.add(sessionId);
    }
    public boolean isBlackListed(String sessionId) {
        return sessionBlackList.contains(sessionId);
    }
}
