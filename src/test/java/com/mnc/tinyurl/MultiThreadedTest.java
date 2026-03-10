package com.mnc.tinyurl;

import com.mnc.tinyurl.generator.SnowflakeIdGenerator;
import com.mnc.tinyurl.service.ShortCodeGeneratorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedTest {

    ShortCodeGeneratorService service = new ShortCodeGeneratorService(new SnowflakeIdGenerator());
    public MultiThreadedTest() {}

    public void test() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                String shortKey = service.generateShortCode("https://example.com");
                System.out.println(Thread.currentThread().getName() + " -> " + shortKey);
            });
        }
        executor.shutdown();
    }

    public static void main(String[] args) {
        new MultiThreadedTest().test();
    }
}