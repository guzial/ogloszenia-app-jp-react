package com.adapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "tekst")
    private String tekst;

    @ManyToOne(fetch = FetchType.LAZY)
    private GrupaTagow grupaTagow;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "seniority", "typUmowy", "wystawca", "tags" }, allowSetters = true)
    private Set<Ogloszenie> ogloszenies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTekst() {
        return this.tekst;
    }

    public Tag tekst(String tekst) {
        this.setTekst(tekst);
        return this;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public GrupaTagow getGrupaTagow() {
        return this.grupaTagow;
    }

    public void setGrupaTagow(GrupaTagow grupaTagow) {
        this.grupaTagow = grupaTagow;
    }

    public Tag grupaTagow(GrupaTagow grupaTagow) {
        this.setGrupaTagow(grupaTagow);
        return this;
    }

    public Set<Ogloszenie> getOgloszenies() {
        return this.ogloszenies;
    }

    public void setOgloszenies(Set<Ogloszenie> ogloszenies) {
        if (this.ogloszenies != null) {
            this.ogloszenies.forEach(i -> i.removeTag(this));
        }
        if (ogloszenies != null) {
            ogloszenies.forEach(i -> i.addTag(this));
        }
        this.ogloszenies = ogloszenies;
    }

    public Tag ogloszenies(Set<Ogloszenie> ogloszenies) {
        this.setOgloszenies(ogloszenies);
        return this;
    }

    public Tag addOgloszenie(Ogloszenie ogloszenie) {
        this.ogloszenies.add(ogloszenie);
        ogloszenie.getTags().add(this);
        return this;
    }

    public Tag removeOgloszenie(Ogloszenie ogloszenie) {
        this.ogloszenies.remove(ogloszenie);
        ogloszenie.getTags().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        return getId() != null && getId().equals(((Tag) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", tekst='" + getTekst() + "'" +
            "}";
    }
}
