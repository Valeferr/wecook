package com.wecook.rest.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {
    public static  String sha256(String password){
        String hashString;

        try {
            MessageDigest messageD = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageD.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hash.length);

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);

                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }

            hashString = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return hashString;
    }
}
