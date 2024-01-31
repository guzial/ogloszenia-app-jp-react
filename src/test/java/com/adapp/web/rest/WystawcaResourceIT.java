package com.adapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adapp.IntegrationTest;
import com.adapp.domain.Wystawca;
import com.adapp.repository.WystawcaRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link WystawcaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WystawcaResourceIT {

    private static final String DEFAULT_NAZWA = "AAAAAAAAAA";
    private static final String UPDATED_NAZWA = "BBBBBBBBBB";

    private static final String DEFAULT_KONTAKT = "AAAAAAAAAA";
    private static final String UPDATED_KONTAKT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/wystawcas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WystawcaRepository wystawcaRepository;

    @Mock
    private WystawcaRepository wystawcaRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWystawcaMockMvc;

    private Wystawca wystawca;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wystawca createEntity(EntityManager em) {
        Wystawca wystawca = new Wystawca().nazwa(DEFAULT_NAZWA).kontakt(DEFAULT_KONTAKT);
        return wystawca;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wystawca createUpdatedEntity(EntityManager em) {
        Wystawca wystawca = new Wystawca().nazwa(UPDATED_NAZWA).kontakt(UPDATED_KONTAKT);
        return wystawca;
    }

    @BeforeEach
    public void initTest() {
        wystawca = createEntity(em);
    }

    @Test
    @Transactional
    void createWystawca() throws Exception {
        int databaseSizeBeforeCreate = wystawcaRepository.findAll().size();
        // Create the Wystawca
        restWystawcaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wystawca)))
            .andExpect(status().isCreated());

        // Validate the Wystawca in the database
        List<Wystawca> wystawcaList = wystawcaRepository.findAll();
        assertThat(wystawcaList).hasSize(databaseSizeBeforeCreate + 1);
        Wystawca testWystawca = wystawcaList.get(wystawcaList.size() - 1);
        assertThat(testWystawca.getNazwa()).isEqualTo(DEFAULT_NAZWA);
        assertThat(testWystawca.getKontakt()).isEqualTo(DEFAULT_KONTAKT);
    }

    @Test
    @Transactional
    void createWystawcaWithExistingId() throws Exception {
        // Create the Wystawca with an existing ID
        wystawca.setId(1L);

        int databaseSizeBeforeCreate = wystawcaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWystawcaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wystawca)))
            .andExpect(status().isBadRequest());

        // Validate the Wystawca in the database
        List<Wystawca> wystawcaList = wystawcaRepository.findAll();
        assertThat(wystawcaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWystawcas() throws Exception {
        // Initialize the database
        wystawcaRepository.saveAndFlush(wystawca);

        // Get all the wystawcaList
        restWystawcaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wystawca.getId().intValue())))
            .andExpect(jsonPath("$.[*].nazwa").value(hasItem(DEFAULT_NAZWA)))
            .andExpect(jsonPath("$.[*].kontakt").value(hasItem(DEFAULT_KONTAKT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWystawcasWithEagerRelationshipsIsEnabled() throws Exception {
        when(wystawcaRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWystawcaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(wystawcaRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWystawcasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(wystawcaRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWystawcaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(wystawcaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWystawca() throws Exception {
        // Initialize the database
        wystawcaRepository.saveAndFlush(wystawca);

        // Get the wystawca
        restWystawcaMockMvc
            .perform(get(ENTITY_API_URL_ID, wystawca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wystawca.getId().intValue()))
            .andExpect(jsonPath("$.nazwa").value(DEFAULT_NAZWA))
            .andExpect(jsonPath("$.kontakt").value(DEFAULT_KONTAKT));
    }

    @Test
    @Transactional
    void getNonExistingWystawca() throws Exception {
        // Get the wystawca
        restWystawcaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWystawca() throws Exception {
        // Initialize the database
        wystawcaRepository.saveAndFlush(wystawca);

        int databaseSizeBeforeUpdate = wystawcaRepository.findAll().size();

        // Update the wystawca
        Wystawca updatedWystawca = wystawcaRepository.findById(wystawca.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWystawca are not directly saved in db
        em.detach(updatedWystawca);
        updatedWystawca.nazwa(UPDATED_NAZWA).kontakt(UPDATED_KONTAKT);

        restWystawcaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWystawca.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWystawca))
            )
            .andExpect(status().isOk());

        // Validate the Wystawca in the database
        List<Wystawca> wystawcaList = wystawcaRepository.findAll();
        assertThat(wystawcaList).hasSize(databaseSizeBeforeUpdate);
        Wystawca testWystawca = wystawcaList.get(wystawcaList.size() - 1);
        assertThat(testWystawca.getNazwa()).isEqualTo(UPDATED_NAZWA);
        assertThat(testWystawca.getKontakt()).isEqualTo(UPDATED_KONTAKT);
    }

    @Test
    @Transactional
    void putNonExistingWystawca() throws Exception {
        int databaseSizeBeforeUpdate = wystawcaRepository.findAll().size();
        wystawca.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWystawcaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wystawca.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wystawca))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wystawca in the database
        List<Wystawca> wystawcaList = wystawcaRepository.findAll();
        assertThat(wystawcaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWystawca() throws Exception {
        int databaseSizeBeforeUpdate = wystawcaRepository.findAll().size();
        wystawca.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWystawcaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wystawca))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wystawca in the database
        List<Wystawca> wystawcaList = wystawcaRepository.findAll();
        assertThat(wystawcaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWystawca() throws Exception {
        int databaseSizeBeforeUpdate = wystawcaRepository.findAll().size();
        wystawca.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWystawcaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wystawca)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wystawca in the database
        List<Wystawca> wystawcaList = wystawcaRepository.findAll();
        assertThat(wystawcaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWystawcaWithPatch() throws Exception {
        // Initialize the database
        wystawcaRepository.saveAndFlush(wystawca);

        int databaseSizeBeforeUpdate = wystawcaRepository.findAll().size();

        // Update the wystawca using partial update
        Wystawca partialUpdatedWystawca = new Wystawca();
        partialUpdatedWystawca.setId(wystawca.getId());

        partialUpdatedWystawca.nazwa(UPDATED_NAZWA);

        restWystawcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWystawca.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWystawca))
            )
            .andExpect(status().isOk());

        // Validate the Wystawca in the database
        List<Wystawca> wystawcaList = wystawcaRepository.findAll();
        assertThat(wystawcaList).hasSize(databaseSizeBeforeUpdate);
        Wystawca testWystawca = wystawcaList.get(wystawcaList.size() - 1);
        assertThat(testWystawca.getNazwa()).isEqualTo(UPDATED_NAZWA);
        assertThat(testWystawca.getKontakt()).isEqualTo(DEFAULT_KONTAKT);
    }

    @Test
    @Transactional
    void fullUpdateWystawcaWithPatch() throws Exception {
        // Initialize the database
        wystawcaRepository.saveAndFlush(wystawca);

        int databaseSizeBeforeUpdate = wystawcaRepository.findAll().size();

        // Update the wystawca using partial update
        Wystawca partialUpdatedWystawca = new Wystawca();
        partialUpdatedWystawca.setId(wystawca.getId());

        partialUpdatedWystawca.nazwa(UPDATED_NAZWA).kontakt(UPDATED_KONTAKT);

        restWystawcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWystawca.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWystawca))
            )
            .andExpect(status().isOk());

        // Validate the Wystawca in the database
        List<Wystawca> wystawcaList = wystawcaRepository.findAll();
        assertThat(wystawcaList).hasSize(databaseSizeBeforeUpdate);
        Wystawca testWystawca = wystawcaList.get(wystawcaList.size() - 1);
        assertThat(testWystawca.getNazwa()).isEqualTo(UPDATED_NAZWA);
        assertThat(testWystawca.getKontakt()).isEqualTo(UPDATED_KONTAKT);
    }

    @Test
    @Transactional
    void patchNonExistingWystawca() throws Exception {
        int databaseSizeBeforeUpdate = wystawcaRepository.findAll().size();
        wystawca.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWystawcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wystawca.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wystawca))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wystawca in the database
        List<Wystawca> wystawcaList = wystawcaRepository.findAll();
        assertThat(wystawcaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWystawca() throws Exception {
        int databaseSizeBeforeUpdate = wystawcaRepository.findAll().size();
        wystawca.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWystawcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wystawca))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wystawca in the database
        List<Wystawca> wystawcaList = wystawcaRepository.findAll();
        assertThat(wystawcaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWystawca() throws Exception {
        int databaseSizeBeforeUpdate = wystawcaRepository.findAll().size();
        wystawca.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWystawcaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(wystawca)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wystawca in the database
        List<Wystawca> wystawcaList = wystawcaRepository.findAll();
        assertThat(wystawcaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWystawca() throws Exception {
        // Initialize the database
        wystawcaRepository.saveAndFlush(wystawca);

        int databaseSizeBeforeDelete = wystawcaRepository.findAll().size();

        // Delete the wystawca
        restWystawcaMockMvc
            .perform(delete(ENTITY_API_URL_ID, wystawca.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Wystawca> wystawcaList = wystawcaRepository.findAll();
        assertThat(wystawcaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
