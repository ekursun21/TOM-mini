package com.TOM.tom_mini.crm.other;

import java.security.SecureRandom;
import java.time.Instant;

public class IdGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static long generate() {return generate(8);}

    public static long generate(int length) {

        // Calculate the base timestamp part
        long timestampPart = Instant.now().toEpochMilli();

        // Trim the timestamp to fit the desired length if necessary
        String timestampStr = Long.toString(timestampPart);
        if (timestampStr.length() > length) {
            timestampStr = timestampStr.substring(0, length);
        }

        // Generate random digits to fill the rest of the length
        int remainingLength = length - timestampStr.length();
        StringBuilder randomPart = new StringBuilder();
        for (int i = 0; i < remainingLength; i++) {
            randomPart.append(random.nextInt(10)); // Append random digits (0-9)
        }

        // Combine the timestamp and random parts
        String idStr = timestampStr + randomPart.toString();

        return Long.parseLong(idStr);
    }
}
