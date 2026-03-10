package com.mnc.tinyurl.util;

public class Base62Encoder {

    private static final char[] BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private static final int CODE_LENGTH = 7;

    public static String encode(long value) {

        StringBuilder sb = new StringBuilder();

        while (value > 0) {
            int remainder = (int) (value % 62);
            sb.append(BASE62[remainder]);
            value /= 62;
        }


        String code = sb.reverse().toString();

        // left pad if shorter than 7
        if (code.length() < CODE_LENGTH) {
            code = "0".repeat(CODE_LENGTH - code.length()) + code;
        }

        return code;
    }
}
