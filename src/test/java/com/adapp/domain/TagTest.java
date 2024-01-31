package com.adapp.domain;

import static com.adapp.domain.GrupaTagowTestSamples.*;
import static com.adapp.domain.OgloszenieTestSamples.*;
import static com.adapp.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.adapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tag.class);
        Tag tag1 = getTagSample1();
        Tag tag2 = new Tag();
        assertThat(tag1).isNotEqualTo(tag2);

        tag2.setId(tag1.getId());
        assertThat(tag1).isEqualTo(tag2);

        tag2 = getTagSample2();
        assertThat(tag1).isNotEqualTo(tag2);
    }

    @Test
    void grupaTagowTest() throws Exception {
        Tag tag = getTagRandomSampleGenerator();
        GrupaTagow grupaTagowBack = getGrupaTagowRandomSampleGenerator();

        tag.setGrupaTagow(grupaTagowBack);
        assertThat(tag.getGrupaTagow()).isEqualTo(grupaTagowBack);

        tag.grupaTagow(null);
        assertThat(tag.getGrupaTagow()).isNull();
    }

    @Test
    void ogloszenieTest() throws Exception {
        Tag tag = getTagRandomSampleGenerator();
        Ogloszenie ogloszenieBack = getOgloszenieRandomSampleGenerator();

        tag.addOgloszenie(ogloszenieBack);
        assertThat(tag.getOgloszenies()).containsOnly(ogloszenieBack);
        assertThat(ogloszenieBack.getTags()).containsOnly(tag);

        tag.removeOgloszenie(ogloszenieBack);
        assertThat(tag.getOgloszenies()).doesNotContain(ogloszenieBack);
        assertThat(ogloszenieBack.getTags()).doesNotContain(tag);

        tag.ogloszenies(new HashSet<>(Set.of(ogloszenieBack)));
        assertThat(tag.getOgloszenies()).containsOnly(ogloszenieBack);
        assertThat(ogloszenieBack.getTags()).containsOnly(tag);

        tag.setOgloszenies(new HashSet<>());
        assertThat(tag.getOgloszenies()).doesNotContain(ogloszenieBack);
        assertThat(ogloszenieBack.getTags()).doesNotContain(tag);
    }
}
