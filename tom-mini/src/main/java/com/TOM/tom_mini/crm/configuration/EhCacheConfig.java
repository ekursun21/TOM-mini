package com.TOM.tom_mini.crm.configuration;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;

import java.io.File;

public class EhCacheConfig {
    public CacheManager createCacheManager() {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence(new File(getStoragePath(), "ehcache")))
                .withCache("persistentCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                        .heap(10, MemoryUnit.MB)
                                        .disk(20, MemoryUnit.MB, true))
                )
                .build(true);
        return cacheManager;
    }

    private String getStoragePath() {
        return "path/to/storage"; // Specify the path where cache data will be stored
    }
}
