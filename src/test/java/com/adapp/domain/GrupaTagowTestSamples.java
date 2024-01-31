package com.adapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GrupaTagowTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static GrupaTagow getGrupaTagowSample1() {
        return new GrupaTagow().id(1L).nazwaGrupy("nazwaGrupy1");
    }

    public static GrupaTagow getGrupaTagowSample2() {
        return new GrupaTagow().id(2L).nazwaGrupy("nazwaGrupy2");
    }

    public static GrupaTagow getGrupaTagowRandomSampleGenerator() {
        return new GrupaTagow().id(longCount.incrementAndGet()).nazwaGrupy(UUID.randomUUID().toString());
    }
}
