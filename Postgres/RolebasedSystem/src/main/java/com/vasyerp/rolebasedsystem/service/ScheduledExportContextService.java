package com.vasyerp.rolebasedsystem.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ScheduledExportContextService {

    private final ConcurrentHashMap<Long, ExportContext> contextByUser = new ConcurrentHashMap<>();

    public void updateContext(Long userId, String userType) {
        if (userId == null || userType == null || userType.isBlank()) {
            return;
        }
        contextByUser.put(userId, new ExportContext(userId, userType));
    }

    public ExportContext getContext() {
        return contextByUser.values().stream().findFirst().orElse(null);
    }

    public List<ExportContext> getAllContexts() {
        return List.copyOf(contextByUser.values());
    }

    public void clearIfMatches(Long userId) {
        if (userId == null) {
            return;
        }
        contextByUser.remove(userId);
    }

    public record ExportContext(Long userId, String userType) {}
}
