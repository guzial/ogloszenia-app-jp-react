package com.adapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypUmowyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypUmowy getTypUmowySample1() {
        return new TypUmowy().id(1L).tekst("tekst1");
    }

    public static TypUmowy getTypUmowySample2() {
        return new TypUmowy().id(2L).tekst("tekst2");
    }

    public static TypUmowy getTypUmowyRandomSampleGenerator() {
        return new TypUmowy().id(longCount.incrementAndGet()).tekst(UUID.randomUUID().toString());
    }
}
