package com.adapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ogloszenie.
 */
@Entity
@Table(name = "ogloszenie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ogloszenie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "tytul")
    private String tytul;

    @Column(name = "opis")
    private String opis;

    @Column(name = "data_publikacji")
    private LocalDate dataPublikacji;

    @Column(name = "data_waznosci")
    private LocalDate dataWaznosci;

    @Column(name = "start_od")
    private LocalDate startOd;

    @Column(name = "czy_widelki")
    private Boolean czyWidelki;

    @Column(name = "widelki_min", precision = 21, scale = 2)
    private BigDecimal widelkiMin;

    @Column(name = "widelki_max", precision = 21, scale = 2)
    private BigDecimal widelkiMax;

    @Column(name = "aktywne")
    private Boolean aktywne;

    @ManyToOne(fetch = FetchType.LAZY)
    private Seniority seniority;

    @ManyToOne(fetch = FetchType.LAZY)
    private TypUmowy typUmowy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Wystawca wystawca;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_ogloszenie__tag",
        joinColumns = @JoinColumn(name = "ogloszenie_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "grupaTagow", "ogloszenies" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ogloszenie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTytul() {
        return this.tytul;
    }

    public Ogloszenie tytul(String tytul) {
        this.setTytul(tytul);
        return this;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getOpis() {
        return this.opis;
    }

    public Ogloszenie opis(String opis) {
        this.setOpis(opis);
        return this;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public LocalDate getDataPublikacji() {
        return this.dataPublikacji;
    }

    public Ogloszenie dataPublikacji(LocalDate dataPublikacji) {
        this.setDataPublikacji(dataPublikacji);
        return this;
    }

    public void setDataPublikacji(LocalDate dataPublikacji) {
        this.dataPublikacji = dataPublikacji;
    }

    public LocalDate getDataWaznosci() {
        return this.dataWaznosci;
    }

    public Ogloszenie dataWaznosci(LocalDate dataWaznosci) {
        this.setDataWaznosci(dataWaznosci);
        return this;
    }

    public void setDataWaznosci(LocalDate dataWaznosci) {
        this.dataWaznosci = dataWaznosci;
    }

    public LocalDate getStartOd() {
        return this.startOd;
    }

    public Ogloszenie startOd(LocalDate startOd) {
        this.setStartOd(startOd);
        return this;
    }

    public void setStartOd(LocalDate startOd) {
        this.startOd = startOd;
    }

    public Boolean getCzyWidelki() {
        return this.czyWidelki;
    }

    public Ogloszenie czyWidelki(Boolean czyWidelki) {
        this.setCzyWidelki(czyWidelki);
        return this;
    }

    public void setCzyWidelki(Boolean czyWidelki) {
        this.czyWidelki = czyWidelki;
    }

    public BigDecimal getWidelkiMin() {
        return this.widelkiMin;
    }

    public Ogloszenie widelkiMin(BigDecimal widelkiMin) {
        this.setWidelkiMin(widelkiMin);
        return this;
    }

    public void setWidelkiMin(BigDecimal widelkiMin) {
        this.widelkiMin = widelkiMin;
    }

    public BigDecimal getWidelkiMax() {
        return this.widelkiMax;
    }

    public Ogloszenie widelkiMax(BigDecimal widelkiMax) {
        this.setWidelkiMax(widelkiMax);
        return this;
    }

    public void setWidelkiMax(BigDecimal widelkiMax) {
        this.widelkiMax = widelkiMax;
    }

    public Boolean getAktywne() {
        return this.aktywne;
    }

    public Ogloszenie aktywne(Boolean aktywne) {
        this.setAktywne(aktywne);
        return this;
    }

    public void setAktywne(Boolean aktywne) {
        this.aktywne = aktywne;
    }

    public Seniority getSeniority() {
        return this.seniority;
    }

    public void setSeniority(Seniority seniority) {
        this.seniority = seniority;
    }

    public Ogloszenie seniority(Seniority seniority) {
        this.setSeniority(seniority);
        return this;
    }

    public TypUmowy getTypUmowy() {
        return this.typUmowy;
    }

    public void setTypUmowy(TypUmowy typUmowy) {
        this.typUmowy = typUmowy;
    }

    public Ogloszenie typUmowy(TypUmowy typUmowy) {
        this.setTypUmowy(typUmowy);
        return this;
    }

    public Wystawca getWystawca() {
        return this.wystawca;
    }

    public void setWystawca(Wystawca wystawca) {
        this.wystawca = wystawca;
    }

    public Ogloszenie wystawca(Wystawca wystawca) {
        this.setWystawca(wystawca);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Ogloszenie tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Ogloszenie addTag(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    public Ogloszenie removeTag(Tag tag) {
        this.tags.remove(tag);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ogloszenie)) {
            return false;
        }
        return getId() != null && getId().equals(((Ogloszenie) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ogloszenie{" +
            "id=" + getId() +
            ", tytul='" + getTytul() + "'" +
            ", opis='" + getOpis() + "'" +
            ", dataPublikacji='" + getDataPublikacji() + "'" +
            ", dataWaznosci='" + getDataWaznosci() + "'" +
            ", startOd='" + getStartOd() + "'" +
            ", czyWidelki='" + getCzyWidelki() + "'" +
            ", widelkiMin=" + getWidelkiMin() +
            ", widelkiMax=" + getWidelkiMax() +
            ", aktywne='" + getAktywne() + "'" +
            "}";
    }
}
