package com.stockcandles.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Value("${cache.caffeine.spec:maximumSize=500,expireAfterWrite=5m}")
    private String caffeineSpec;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("stockCandles");
        cacheManager.setCaffeine(parseCaffeineSpec());
        return cacheManager;
    }

    private Caffeine<Object, Object> parseCaffeineSpec() {
        // Placeholder parser; defaults used until business logic requires custom tuning
        return Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(5, TimeUnit.MINUTES);
    }
}
