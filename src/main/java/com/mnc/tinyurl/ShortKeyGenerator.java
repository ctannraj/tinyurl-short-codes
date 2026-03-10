package com.mnc.tinyurl;

class ShortKeyGenerator {

    private static final int SHORT_KEY_LENGTH = 7;

    public static String generate(long snowflakeId) {
        long scrambled = IdScrambler.scramble(snowflakeId);
        String base62 = Base62Encoder.encode(scrambled);
        if (base62.length() >= SHORT_KEY_LENGTH) {
            return base62.substring(base62.length() - SHORT_KEY_LENGTH);
        }

        StringBuilder padded = new StringBuilder();
        for (int i = 0; i < SHORT_KEY_LENGTH - base62.length(); i++) {
            padded.append('0');
        }
        padded.append(base62);
        return padded.toString();
    }
}
