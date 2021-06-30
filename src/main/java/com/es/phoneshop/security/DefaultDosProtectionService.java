package com.es.phoneshop.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long THRESHOLD = 100;

    private final Map<String, Long> counters = new ConcurrentHashMap<>();

    public static DefaultDosProtectionService getInstance() {
        return SingletonInstanceHolder.instance;
    }

    private DefaultDosProtectionService() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(counters::clear, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public boolean isAllowed(String ip) {
        Long counter = counters.getOrDefault(ip, 0L);
        if (counter > THRESHOLD) {
            return false;
        } else {
            counters.put(ip, counter + 1);
            return true;
        }
    }

    private static class SingletonInstanceHolder {
        private static final DefaultDosProtectionService instance = new DefaultDosProtectionService();
    }
}
