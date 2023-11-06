package com.weihubeats.boot.cache.config;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author : wh
 * @date : 2023/6/8 18:18
 * @description:
 */
@Configuration
public class CacheConfig {

    @Bean
    @Primary
    public CacheManager cacheManager() {
        
        
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        ArrayList<CaffeineCache> caffeineCaches = new ArrayList<>();

        CaffeineCache cache = new CaffeineCache("student", Caffeine.newBuilder()
                .expireAfterWrite(100, TimeUnit.MINUTES).maximumSize(100).build());
        caffeineCaches.add(cache);
        
        simpleCacheManager.setCaches(caffeineCaches);
        return simpleCacheManager;
    }

}
