package org.naukma.analytics.utils;

import java.util.Random;

public class Common {

    public static String getRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }

    public static long getRandomLong(long min, long max) {
        Random random = new Random();
        return random.nextLong((max - min) + 1) + min;
    }
}
