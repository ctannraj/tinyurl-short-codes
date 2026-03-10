package com.mnc.tinyurl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedTest {

    public static void main(String[] args) {
        SnowflakeGenerator generator = new SnowflakeGenerator(1);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                long id = generator.nextId();
                String shortKey = ShortKeyGenerator.generate(id);
                System.out.println(Thread.currentThread().getName() + " -> " + shortKey);
            });
        }
        executor.shutdown();
    }
}