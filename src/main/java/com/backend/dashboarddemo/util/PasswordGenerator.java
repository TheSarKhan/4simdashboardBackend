package com.backend.dashboarddemo.util;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String PASSWORD_ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";

    public static String generateRandomPassword(int length) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);
        int alphabetLength = PASSWORD_ALPHABET.length();

        for (int i = 0; i < length; i++) {
            int nexted = secureRandom.nextInt(alphabetLength);
            stringBuilder.append(PASSWORD_ALPHABET.charAt(nexted));
        }
        return stringBuilder.toString();
    }
}
