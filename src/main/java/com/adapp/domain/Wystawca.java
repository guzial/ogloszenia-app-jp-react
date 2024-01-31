package com.adapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Wystawca.
 */
@Entity
@Table(name = "wystawca")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Wystawca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nazwa")
    private String nazwa;

    @Column(name = "kontakt")
    private String kontakt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Wystawca id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazwa() {
        return this.nazwa;
    }

    public Wystawca nazwa(String nazwa) {
        this.setNazwa(nazwa);
        return this;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getKontakt() {
        return this.kontakt;
    }

    public Wystawca kontakt(String kontakt) {
        this.setKontakt(kontakt);
        return this;
    }

    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Wystawca user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Wystawca)) {
            return false;
        }
        return getId() != null && getId().equals(((Wystawca) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Wystawca{" +
            "id=" + getId() +
            ", nazwa='" + getNazwa() + "'" +
            ", kontakt='" + getKontakt() + "'" +
            "}";
    }
}
