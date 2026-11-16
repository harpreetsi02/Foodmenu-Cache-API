package com.backend.foodmenu.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(){
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(List.of(
                build(CacheNames.CITIES, Duration.ofHours(24), 100),
                build(CacheNames.CUISINES, Duration.ofHours(24), 100),
                build(CacheNames.RESTAURANTS, Duration.ofMinutes(30), 1_000),
                build(CacheNames.MENU_ITEMS, Duration.ofMinutes(5), 5_000),
                build(CacheNames.ACTIVE_OFFERS, Duration.ofMinutes(1), 1_000)
        ));

        return manager;
    }

    private CaffeineCache build(String name, Duration ttl, long maxSize){
        return  new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterWrite(ttl)
                .maximumSize(maxSize)
                .recordStats()
                .build());
    }
}
