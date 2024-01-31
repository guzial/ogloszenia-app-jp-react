package com.adapp.web.rest;

import static com.adapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adapp.IntegrationTest;
import com.adapp.domain.Ogloszenie;
import com.adapp.repository.OgloszenieRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OgloszenieResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OgloszenieResourceIT {

    private static final String DEFAULT_TYTUL = "AAAAAAAAAA";
    private static final String UPDATED_TYTUL = "BBBBBBBBBB";

    private static final String DEFAULT_OPIS = "AAAAAAAAAA";
    private static final String UPDATED_OPIS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_PUBLIKACJI = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_PUBLIKACJI = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_WAZNOSCI = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_WAZNOSCI = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_START_OD = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_OD = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_CZY_WIDELKI = false;
    private static final Boolean UPDATED_CZY_WIDELKI = true;

    private static final BigDecimal DEFAULT_WIDELKI_MIN = new BigDecimal(1);
    private static final BigDecimal UPDATED_WIDELKI_MIN = new BigDecimal(2);

    private static final BigDecimal DEFAULT_WIDELKI_MAX = new BigDecimal(1);
    private static final BigDecimal UPDATED_WIDELKI_MAX = new BigDecimal(2);

    private static final Boolean DEFAULT_AKTYWNE = false;
    private static final Boolean UPDATED_AKTYWNE = true;

    private static final String ENTITY_API_URL = "/api/ogloszenies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OgloszenieRepository ogloszenieRepository;

    @Mock
    private OgloszenieRepository ogloszenieRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOgloszenieMockMvc;

    private Ogloszenie ogloszenie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ogloszenie createEntity(EntityManager em) {
        Ogloszenie ogloszenie = new Ogloszenie()
            .tytul(DEFAULT_TYTUL)
            .opis(DEFAULT_OPIS)
            .dataPublikacji(DEFAULT_DATA_PUBLIKACJI)
            .dataWaznosci(DEFAULT_DATA_WAZNOSCI)
            .startOd(DEFAULT_START_OD)
            .czyWidelki(DEFAULT_CZY_WIDELKI)
            .widelkiMin(DEFAULT_WIDELKI_MIN)
            .widelkiMax(DEFAULT_WIDELKI_MAX)
            .aktywne(DEFAULT_AKTYWNE);
        return ogloszenie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ogloszenie createUpdatedEntity(EntityManager em) {
        Ogloszenie ogloszenie = new Ogloszenie()
            .tytul(UPDATED_TYTUL)
            .opis(UPDATED_OPIS)
            .dataPublikacji(UPDATED_DATA_PUBLIKACJI)
            .dataWaznosci(UPDATED_DATA_WAZNOSCI)
            .startOd(UPDATED_START_OD)
            .czyWidelki(UPDATED_CZY_WIDELKI)
            .widelkiMin(UPDATED_WIDELKI_MIN)
            .widelkiMax(UPDATED_WIDELKI_MAX)
            .aktywne(UPDATED_AKTYWNE);
        return ogloszenie;
    }

    @BeforeEach
    public void initTest() {
        ogloszenie = createEntity(em);
    }

    @Test
    @Transactional
    void createOgloszenie() throws Exception {
        int databaseSizeBeforeCreate = ogloszenieRepository.findAll().size();
        // Create the Ogloszenie
        restOgloszenieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ogloszenie)))
            .andExpect(status().isCreated());

        // Validate the Ogloszenie in the database
        List<Ogloszenie> ogloszenieList = ogloszenieRepository.findAll();
        assertThat(ogloszenieList).hasSize(databaseSizeBeforeCreate + 1);
        Ogloszenie testOgloszenie = ogloszenieList.get(ogloszenieList.size() - 1);
        assertThat(testOgloszenie.getTytul()).isEqualTo(DEFAULT_TYTUL);
        assertThat(testOgloszenie.getOpis()).isEqualTo(DEFAULT_OPIS);
        assertThat(testOgloszenie.getDataPublikacji()).isEqualTo(DEFAULT_DATA_PUBLIKACJI);
        assertThat(testOgloszenie.getDataWaznosci()).isEqualTo(DEFAULT_DATA_WAZNOSCI);
        assertThat(testOgloszenie.getStartOd()).isEqualTo(DEFAULT_START_OD);
        assertThat(testOgloszenie.getCzyWidelki()).isEqualTo(DEFAULT_CZY_WIDELKI);
        assertThat(testOgloszenie.getWidelkiMin()).isEqualByComparingTo(DEFAULT_WIDELKI_MIN);
        assertThat(testOgloszenie.getWidelkiMax()).isEqualByComparingTo(DEFAULT_WIDELKI_MAX);
        assertThat(testOgloszenie.getAktywne()).isEqualTo(DEFAULT_AKTYWNE);
    }

    @Test
    @Transactional
    void createOgloszenieWithExistingId() throws Exception {
        // Create the Ogloszenie with an existing ID
        ogloszenie.setId(1L);

        int databaseSizeBeforeCreate = ogloszenieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOgloszenieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ogloszenie)))
            .andExpect(status().isBadRequest());

        // Validate the Ogloszenie in the database
        List<Ogloszenie> ogloszenieList = ogloszenieRepository.findAll();
        assertThat(ogloszenieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOgloszenies() throws Exception {
        // Initialize the database
        ogloszenieRepository.saveAndFlush(ogloszenie);

        // Get all the ogloszenieList
        restOgloszenieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ogloszenie.getId().intValue())))
            .andExpect(jsonPath("$.[*].tytul").value(hasItem(DEFAULT_TYTUL)))
            .andExpect(jsonPath("$.[*].opis").value(hasItem(DEFAULT_OPIS)))
            .andExpect(jsonPath("$.[*].dataPublikacji").value(hasItem(DEFAULT_DATA_PUBLIKACJI.toString())))
            .andExpect(jsonPath("$.[*].dataWaznosci").value(hasItem(DEFAULT_DATA_WAZNOSCI.toString())))
            .andExpect(jsonPath("$.[*].startOd").value(hasItem(DEFAULT_START_OD.toString())))
            .andExpect(jsonPath("$.[*].czyWidelki").value(hasItem(DEFAULT_CZY_WIDELKI.booleanValue())))
            .andExpect(jsonPath("$.[*].widelkiMin").value(hasItem(sameNumber(DEFAULT_WIDELKI_MIN))))
            .andExpect(jsonPath("$.[*].widelkiMax").value(hasItem(sameNumber(DEFAULT_WIDELKI_MAX))))
            .andExpect(jsonPath("$.[*].aktywne").value(hasItem(DEFAULT_AKTYWNE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOgloszeniesWithEagerRelationshipsIsEnabled() throws Exception {
        when(ogloszenieRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOgloszenieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ogloszenieRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOgloszeniesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ogloszenieRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOgloszenieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ogloszenieRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOgloszenie() throws Exception {
        // Initialize the database
        ogloszenieRepository.saveAndFlush(ogloszenie);

        // Get the ogloszenie
        restOgloszenieMockMvc
            .perform(get(ENTITY_API_URL_ID, ogloszenie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ogloszenie.getId().intValue()))
            .andExpect(jsonPath("$.tytul").value(DEFAULT_TYTUL))
            .andExpect(jsonPath("$.opis").value(DEFAULT_OPIS))
            .andExpect(jsonPath("$.dataPublikacji").value(DEFAULT_DATA_PUBLIKACJI.toString()))
            .andExpect(jsonPath("$.dataWaznosci").value(DEFAULT_DATA_WAZNOSCI.toString()))
            .andExpect(jsonPath("$.startOd").value(DEFAULT_START_OD.toString()))
            .andExpect(jsonPath("$.czyWidelki").value(DEFAULT_CZY_WIDELKI.booleanValue()))
            .andExpect(jsonPath("$.widelkiMin").value(sameNumber(DEFAULT_WIDELKI_MIN)))
            .andExpect(jsonPath("$.widelkiMax").value(sameNumber(DEFAULT_WIDELKI_MAX)))
            .andExpect(jsonPath("$.aktywne").value(DEFAULT_AKTYWNE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingOgloszenie() throws Exception {
        // Get the ogloszenie
        restOgloszenieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOgloszenie() throws Exception {
        // Initialize the database
        ogloszenieRepository.saveAndFlush(ogloszenie);

        int databaseSizeBeforeUpdate = ogloszenieRepository.findAll().size();

        // Update the ogloszenie
        Ogloszenie updatedOgloszenie = ogloszenieRepository.findById(ogloszenie.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOgloszenie are not directly saved in db
        em.detach(updatedOgloszenie);
        updatedOgloszenie
            .tytul(UPDATED_TYTUL)
            .opis(UPDATED_OPIS)
            .dataPublikacji(UPDATED_DATA_PUBLIKACJI)
            .dataWaznosci(UPDATED_DATA_WAZNOSCI)
            .startOd(UPDATED_START_OD)
            .czyWidelki(UPDATED_CZY_WIDELKI)
            .widelkiMin(UPDATED_WIDELKI_MIN)
            .widelkiMax(UPDATED_WIDELKI_MAX)
            .aktywne(UPDATED_AKTYWNE);

        restOgloszenieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOgloszenie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOgloszenie))
            )
            .andExpect(status().isOk());

        // Validate the Ogloszenie in the database
        List<Ogloszenie> ogloszenieList = ogloszenieRepository.findAll();
        assertThat(ogloszenieList).hasSize(databaseSizeBeforeUpdate);
        Ogloszenie testOgloszenie = ogloszenieList.get(ogloszenieList.size() - 1);
        assertThat(testOgloszenie.getTytul()).isEqualTo(UPDATED_TYTUL);
        assertThat(testOgloszenie.getOpis()).isEqualTo(UPDATED_OPIS);
        assertThat(testOgloszenie.getDataPublikacji()).isEqualTo(UPDATED_DATA_PUBLIKACJI);
        assertThat(testOgloszenie.getDataWaznosci()).isEqualTo(UPDATED_DATA_WAZNOSCI);
        assertThat(testOgloszenie.getStartOd()).isEqualTo(UPDATED_START_OD);
        assertThat(testOgloszenie.getCzyWidelki()).isEqualTo(UPDATED_CZY_WIDELKI);
        assertThat(testOgloszenie.getWidelkiMin()).isEqualByComparingTo(UPDATED_WIDELKI_MIN);
        assertThat(testOgloszenie.getWidelkiMax()).isEqualByComparingTo(UPDATED_WIDELKI_MAX);
        assertThat(testOgloszenie.getAktywne()).isEqualTo(UPDATED_AKTYWNE);
    }

    @Test
    @Transactional
    void putNonExistingOgloszenie() throws Exception {
        int databaseSizeBeforeUpdate = ogloszenieRepository.findAll().size();
        ogloszenie.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOgloszenieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ogloszenie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ogloszenie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ogloszenie in the database
        List<Ogloszenie> ogloszenieList = ogloszenieRepository.findAll();
        assertThat(ogloszenieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOgloszenie() throws Exception {
        int databaseSizeBeforeUpdate = ogloszenieRepository.findAll().size();
        ogloszenie.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgloszenieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ogloszenie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ogloszenie in the database
        List<Ogloszenie> ogloszenieList = ogloszenieRepository.findAll();
        assertThat(ogloszenieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOgloszenie() throws Exception {
        int databaseSizeBeforeUpdate = ogloszenieRepository.findAll().size();
        ogloszenie.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgloszenieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ogloszenie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ogloszenie in the database
        List<Ogloszenie> ogloszenieList = ogloszenieRepository.findAll();
        assertThat(ogloszenieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOgloszenieWithPatch() throws Exception {
        // Initialize the database
        ogloszenieRepository.saveAndFlush(ogloszenie);

        int databaseSizeBeforeUpdate = ogloszenieRepository.findAll().size();

        // Update the ogloszenie using partial update
        Ogloszenie partialUpdatedOgloszenie = new Ogloszenie();
        partialUpdatedOgloszenie.setId(ogloszenie.getId());

        partialUpdatedOgloszenie
            .opis(UPDATED_OPIS)
            .dataPublikacji(UPDATED_DATA_PUBLIKACJI)
            .dataWaznosci(UPDATED_DATA_WAZNOSCI)
            .startOd(UPDATED_START_OD)
            .aktywne(UPDATED_AKTYWNE);

        restOgloszenieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOgloszenie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOgloszenie))
            )
            .andExpect(status().isOk());

        // Validate the Ogloszenie in the database
        List<Ogloszenie> ogloszenieList = ogloszenieRepository.findAll();
        assertThat(ogloszenieList).hasSize(databaseSizeBeforeUpdate);
        Ogloszenie testOgloszenie = ogloszenieList.get(ogloszenieList.size() - 1);
        assertThat(testOgloszenie.getTytul()).isEqualTo(DEFAULT_TYTUL);
        assertThat(testOgloszenie.getOpis()).isEqualTo(UPDATED_OPIS);
        assertThat(testOgloszenie.getDataPublikacji()).isEqualTo(UPDATED_DATA_PUBLIKACJI);
        assertThat(testOgloszenie.getDataWaznosci()).isEqualTo(UPDATED_DATA_WAZNOSCI);
        assertThat(testOgloszenie.getStartOd()).isEqualTo(UPDATED_START_OD);
        assertThat(testOgloszenie.getCzyWidelki()).isEqualTo(DEFAULT_CZY_WIDELKI);
        assertThat(testOgloszenie.getWidelkiMin()).isEqualByComparingTo(DEFAULT_WIDELKI_MIN);
        assertThat(testOgloszenie.getWidelkiMax()).isEqualByComparingTo(DEFAULT_WIDELKI_MAX);
        assertThat(testOgloszenie.getAktywne()).isEqualTo(UPDATED_AKTYWNE);
    }

    @Test
    @Transactional
    void fullUpdateOgloszenieWithPatch() throws Exception {
        // Initialize the database
        ogloszenieRepository.saveAndFlush(ogloszenie);

        int databaseSizeBeforeUpdate = ogloszenieRepository.findAll().size();

        // Update the ogloszenie using partial update
        Ogloszenie partialUpdatedOgloszenie = new Ogloszenie();
        partialUpdatedOgloszenie.setId(ogloszenie.getId());

        partialUpdatedOgloszenie
            .tytul(UPDATED_TYTUL)
            .opis(UPDATED_OPIS)
            .dataPublikacji(UPDATED_DATA_PUBLIKACJI)
            .dataWaznosci(UPDATED_DATA_WAZNOSCI)
            .startOd(UPDATED_START_OD)
            .czyWidelki(UPDATED_CZY_WIDELKI)
            .widelkiMin(UPDATED_WIDELKI_MIN)
            .widelkiMax(UPDATED_WIDELKI_MAX)
            .aktywne(UPDATED_AKTYWNE);

        restOgloszenieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOgloszenie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOgloszenie))
            )
            .andExpect(status().isOk());

        // Validate the Ogloszenie in the database
        List<Ogloszenie> ogloszenieList = ogloszenieRepository.findAll();
        assertThat(ogloszenieList).hasSize(databaseSizeBeforeUpdate);
        Ogloszenie testOgloszenie = ogloszenieList.get(ogloszenieList.size() - 1);
        assertThat(testOgloszenie.getTytul()).isEqualTo(UPDATED_TYTUL);
        assertThat(testOgloszenie.getOpis()).isEqualTo(UPDATED_OPIS);
        assertThat(testOgloszenie.getDataPublikacji()).isEqualTo(UPDATED_DATA_PUBLIKACJI);
        assertThat(testOgloszenie.getDataWaznosci()).isEqualTo(UPDATED_DATA_WAZNOSCI);
        assertThat(testOgloszenie.getStartOd()).isEqualTo(UPDATED_START_OD);
        assertThat(testOgloszenie.getCzyWidelki()).isEqualTo(UPDATED_CZY_WIDELKI);
        assertThat(testOgloszenie.getWidelkiMin()).isEqualByComparingTo(UPDATED_WIDELKI_MIN);
        assertThat(testOgloszenie.getWidelkiMax()).isEqualByComparingTo(UPDATED_WIDELKI_MAX);
        assertThat(testOgloszenie.getAktywne()).isEqualTo(UPDATED_AKTYWNE);
    }

    @Test
    @Transactional
    void patchNonExistingOgloszenie() throws Exception {
        int databaseSizeBeforeUpdate = ogloszenieRepository.findAll().size();
        ogloszenie.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOgloszenieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ogloszenie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ogloszenie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ogloszenie in the database
        List<Ogloszenie> ogloszenieList = ogloszenieRepository.findAll();
        assertThat(ogloszenieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOgloszenie() throws Exception {
        int databaseSizeBeforeUpdate = ogloszenieRepository.findAll().size();
        ogloszenie.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgloszenieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ogloszenie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ogloszenie in the database
        List<Ogloszenie> ogloszenieList = ogloszenieRepository.findAll();
        assertThat(ogloszenieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOgloszenie() throws Exception {
        int databaseSizeBeforeUpdate = ogloszenieRepository.findAll().size();
        ogloszenie.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgloszenieMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ogloszenie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ogloszenie in the database
        List<Ogloszenie> ogloszenieList = ogloszenieRepository.findAll();
        assertThat(ogloszenieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOgloszenie() throws Exception {
        // Initialize the database
        ogloszenieRepository.saveAndFlush(ogloszenie);

        int databaseSizeBeforeDelete = ogloszenieRepository.findAll().size();

        // Delete the ogloszenie
        restOgloszenieMockMvc
            .perform(delete(ENTITY_API_URL_ID, ogloszenie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ogloszenie> ogloszenieList = ogloszenieRepository.findAll();
        assertThat(ogloszenieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
