package com.weihubeats.boot.cache.config;

import java.util.ArrayList;
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
        /*for (Caches c : Caches.values()) {
            caffeineCaches.add(new CaffeineCache(c.name(),
                    Caffeine.newBuilder()
                        .recordStats()
                        .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
                        .maximumSize(c.getMaxSize())
                        .build()
                )
            );
        }*/
        simpleCacheManager.setCaches(caffeineCaches);
        return simpleCacheManager;
    }

}
