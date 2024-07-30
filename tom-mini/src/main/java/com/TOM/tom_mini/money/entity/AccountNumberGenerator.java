package com.TOM.tom_mini.money.entity;

import java.security.SecureRandom;
import java.time.Instant;

public class AccountNumberGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generate() {
        // Prefix with a random letter
        char prefix = ALPHABET.charAt(random.nextInt(ALPHABET.length()));

        // Use the current timestamp in milliseconds to ensure uniqueness
        long timestampPart = Instant.now().toEpochMilli();

        // Add a random number to enhance security and reduce predictability
        int randomPart = random.nextInt(1000); // Three additional digits of randomness

        return String.format("%c%d%03d", prefix, timestampPart, randomPart);
    }
}
