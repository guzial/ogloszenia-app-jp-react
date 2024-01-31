package com.adapp.domain;

import static com.adapp.domain.OgloszenieTestSamples.*;
import static com.adapp.domain.SeniorityTestSamples.*;
import static com.adapp.domain.TagTestSamples.*;
import static com.adapp.domain.TypUmowyTestSamples.*;
import static com.adapp.domain.WystawcaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.adapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OgloszenieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ogloszenie.class);
        Ogloszenie ogloszenie1 = getOgloszenieSample1();
        Ogloszenie ogloszenie2 = new Ogloszenie();
        assertThat(ogloszenie1).isNotEqualTo(ogloszenie2);

        ogloszenie2.setId(ogloszenie1.getId());
        assertThat(ogloszenie1).isEqualTo(ogloszenie2);

        ogloszenie2 = getOgloszenieSample2();
        assertThat(ogloszenie1).isNotEqualTo(ogloszenie2);
    }

    @Test
    void seniorityTest() throws Exception {
        Ogloszenie ogloszenie = getOgloszenieRandomSampleGenerator();
        Seniority seniorityBack = getSeniorityRandomSampleGenerator();

        ogloszenie.setSeniority(seniorityBack);
        assertThat(ogloszenie.getSeniority()).isEqualTo(seniorityBack);

        ogloszenie.seniority(null);
        assertThat(ogloszenie.getSeniority()).isNull();
    }

    @Test
    void typUmowyTest() throws Exception {
        Ogloszenie ogloszenie = getOgloszenieRandomSampleGenerator();
        TypUmowy typUmowyBack = getTypUmowyRandomSampleGenerator();

        ogloszenie.setTypUmowy(typUmowyBack);
        assertThat(ogloszenie.getTypUmowy()).isEqualTo(typUmowyBack);

        ogloszenie.typUmowy(null);
        assertThat(ogloszenie.getTypUmowy()).isNull();
    }

    @Test
    void wystawcaTest() throws Exception {
        Ogloszenie ogloszenie = getOgloszenieRandomSampleGenerator();
        Wystawca wystawcaBack = getWystawcaRandomSampleGenerator();

        ogloszenie.setWystawca(wystawcaBack);
        assertThat(ogloszenie.getWystawca()).isEqualTo(wystawcaBack);

        ogloszenie.wystawca(null);
        assertThat(ogloszenie.getWystawca()).isNull();
    }

    @Test
    void tagTest() throws Exception {
        Ogloszenie ogloszenie = getOgloszenieRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        ogloszenie.addTag(tagBack);
        assertThat(ogloszenie.getTags()).containsOnly(tagBack);

        ogloszenie.removeTag(tagBack);
        assertThat(ogloszenie.getTags()).doesNotContain(tagBack);

        ogloszenie.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(ogloszenie.getTags()).containsOnly(tagBack);

        ogloszenie.setTags(new HashSet<>());
        assertThat(ogloszenie.getTags()).doesNotContain(tagBack);
    }
}
