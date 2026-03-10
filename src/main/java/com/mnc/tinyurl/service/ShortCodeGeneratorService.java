package com.mnc.tinyurl.service;

import com.mnc.tinyurl.generator.SnowflakeIdGenerator;
import com.mnc.tinyurl.util.Base62Encoder;
import com.mnc.tinyurl.util.IdScrambler;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ShortCodeGeneratorService {

    private final SnowflakeIdGenerator snowflakeIdGenerator;

    private final Map<String, String> shortCodeStore = new ConcurrentHashMap<>();

    public ShortCodeGeneratorService(SnowflakeIdGenerator generator) {
        this.snowflakeIdGenerator = generator;
    }

    public String generateShortCode(String longUrl) {
        long snowflakeId = snowflakeIdGenerator.nextId();
        long scrambled = IdScrambler.scramble(snowflakeId);
        String shortCode = Base62Encoder.encode(scrambled);
        shortCodeStore.put(shortCode, longUrl);
        return shortCode;
    }

    public String resolve(String shortCode) {
        return shortCodeStore.get(shortCode);
    }
}
