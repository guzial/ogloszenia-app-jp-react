package com.adapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WystawcaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Wystawca getWystawcaSample1() {
        return new Wystawca().id(1L).nazwa("nazwa1").kontakt("kontakt1");
    }

    public static Wystawca getWystawcaSample2() {
        return new Wystawca().id(2L).nazwa("nazwa2").kontakt("kontakt2");
    }

    public static Wystawca getWystawcaRandomSampleGenerator() {
        return new Wystawca().id(longCount.incrementAndGet()).nazwa(UUID.randomUUID().toString()).kontakt(UUID.randomUUID().toString());
    }
}
