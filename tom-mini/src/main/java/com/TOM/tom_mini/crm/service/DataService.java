package com.TOM.tom_mini.crm.service;

import lombok.Data;
import org.springframework.cache.annotation.CachePut;

public class DataService {

    @CachePut(cacheNames = "dataCache", key = "#data.id")
    public Integer updateFee(Integer data) {
        return data;
    }
}

