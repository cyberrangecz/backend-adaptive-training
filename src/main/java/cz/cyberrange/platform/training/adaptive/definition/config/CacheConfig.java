package cz.cyberrange.platform.training.adaptive.definition.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Objects;

/**
 * Enables caching and scheduling
 */
@EnableCaching
@EnableScheduling
@Configuration
public class CacheConfig {
    private final CacheManager cacheManager;

    public CacheConfig(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Clears cache for instance simulator tool every 24 hours
     */
    @Scheduled(fixedRate = 86400000)
    public void evictAllCaches() {
        cacheManager.getCacheNames().stream()
                .filter(cacheName -> Objects.equals(cacheName, "traineesPerformance"))
                .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }

}
