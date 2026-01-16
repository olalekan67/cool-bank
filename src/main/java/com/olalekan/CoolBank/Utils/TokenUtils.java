package com.olalekan.CoolBank.Utils;

import java.security.SecureRandom;

public class TokenUtils {

    private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateToken(){
        StringBuilder sb = new StringBuilder(7);

        for (int i = 0; i < 7; i++){
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

}
