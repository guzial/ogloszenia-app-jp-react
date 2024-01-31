package com.adapp.domain;

import static com.adapp.domain.WystawcaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.adapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WystawcaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wystawca.class);
        Wystawca wystawca1 = getWystawcaSample1();
        Wystawca wystawca2 = new Wystawca();
        assertThat(wystawca1).isNotEqualTo(wystawca2);

        wystawca2.setId(wystawca1.getId());
        assertThat(wystawca1).isEqualTo(wystawca2);

        wystawca2 = getWystawcaSample2();
        assertThat(wystawca1).isNotEqualTo(wystawca2);
    }
}
