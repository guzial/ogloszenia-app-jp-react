package com.adapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OgloszenieTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ogloszenie getOgloszenieSample1() {
        return new Ogloszenie().id(1L).tytul("tytul1").opis("opis1");
    }

    public static Ogloszenie getOgloszenieSample2() {
        return new Ogloszenie().id(2L).tytul("tytul2").opis("opis2");
    }

    public static Ogloszenie getOgloszenieRandomSampleGenerator() {
        return new Ogloszenie().id(longCount.incrementAndGet()).tytul(UUID.randomUUID().toString()).opis(UUID.randomUUID().toString());
    }
}
