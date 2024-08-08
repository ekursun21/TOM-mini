package com.TOM.tom_mini.money.entity;

import java.security.SecureRandom;
import java.time.Instant;

public class IdGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static long generate() {
        long timestampPart = Instant.now().toEpochMilli();
        int randomPart = random.nextInt(1000);
        return timestampPart * 1000 + randomPart;
    }
}
