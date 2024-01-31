package com.adapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SeniorityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Seniority getSenioritySample1() {
        return new Seniority().id(1L).nazwa("nazwa1");
    }

    public static Seniority getSenioritySample2() {
        return new Seniority().id(2L).nazwa("nazwa2");
    }

    public static Seniority getSeniorityRandomSampleGenerator() {
        return new Seniority().id(longCount.incrementAndGet()).nazwa(UUID.randomUUID().toString());
    }
}
