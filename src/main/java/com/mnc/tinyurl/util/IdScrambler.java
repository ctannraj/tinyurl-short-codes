package com.mnc.tinyurl.util;

public class IdScrambler {

    private static final long SALT = 0x5A3C9B2F4D1L; // secret constant

    public static long scramble(long id) {
        return id ^ SALT;
    }

    public static long unscramble(long id) {
        return id ^ SALT;
    }
}
