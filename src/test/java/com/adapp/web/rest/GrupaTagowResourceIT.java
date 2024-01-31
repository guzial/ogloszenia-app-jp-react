package com.adapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adapp.IntegrationTest;
import com.adapp.domain.GrupaTagow;
import com.adapp.repository.GrupaTagowRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GrupaTagowResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GrupaTagowResourceIT {

    private static final String DEFAULT_NAZWA_GRUPY = "AAAAAAAAAA";
    private static final String UPDATED_NAZWA_GRUPY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/grupa-tagows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GrupaTagowRepository grupaTagowRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGrupaTagowMockMvc;

    private GrupaTagow grupaTagow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GrupaTagow createEntity(EntityManager em) {
        GrupaTagow grupaTagow = new GrupaTagow().nazwaGrupy(DEFAULT_NAZWA_GRUPY);
        return grupaTagow;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GrupaTagow createUpdatedEntity(EntityManager em) {
        GrupaTagow grupaTagow = new GrupaTagow().nazwaGrupy(UPDATED_NAZWA_GRUPY);
        return grupaTagow;
    }

    @BeforeEach
    public void initTest() {
        grupaTagow = createEntity(em);
    }

    @Test
    @Transactional
    void createGrupaTagow() throws Exception {
        int databaseSizeBeforeCreate = grupaTagowRepository.findAll().size();
        // Create the GrupaTagow
        restGrupaTagowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupaTagow)))
            .andExpect(status().isCreated());

        // Validate the GrupaTagow in the database
        List<GrupaTagow> grupaTagowList = grupaTagowRepository.findAll();
        assertThat(grupaTagowList).hasSize(databaseSizeBeforeCreate + 1);
        GrupaTagow testGrupaTagow = grupaTagowList.get(grupaTagowList.size() - 1);
        assertThat(testGrupaTagow.getNazwaGrupy()).isEqualTo(DEFAULT_NAZWA_GRUPY);
    }

    @Test
    @Transactional
    void createGrupaTagowWithExistingId() throws Exception {
        // Create the GrupaTagow with an existing ID
        grupaTagow.setId(1L);

        int databaseSizeBeforeCreate = grupaTagowRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGrupaTagowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupaTagow)))
            .andExpect(status().isBadRequest());

        // Validate the GrupaTagow in the database
        List<GrupaTagow> grupaTagowList = grupaTagowRepository.findAll();
        assertThat(grupaTagowList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGrupaTagows() throws Exception {
        // Initialize the database
        grupaTagowRepository.saveAndFlush(grupaTagow);

        // Get all the grupaTagowList
        restGrupaTagowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grupaTagow.getId().intValue())))
            .andExpect(jsonPath("$.[*].nazwaGrupy").value(hasItem(DEFAULT_NAZWA_GRUPY)));
    }

    @Test
    @Transactional
    void getGrupaTagow() throws Exception {
        // Initialize the database
        grupaTagowRepository.saveAndFlush(grupaTagow);

        // Get the grupaTagow
        restGrupaTagowMockMvc
            .perform(get(ENTITY_API_URL_ID, grupaTagow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(grupaTagow.getId().intValue()))
            .andExpect(jsonPath("$.nazwaGrupy").value(DEFAULT_NAZWA_GRUPY));
    }

    @Test
    @Transactional
    void getNonExistingGrupaTagow() throws Exception {
        // Get the grupaTagow
        restGrupaTagowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGrupaTagow() throws Exception {
        // Initialize the database
        grupaTagowRepository.saveAndFlush(grupaTagow);

        int databaseSizeBeforeUpdate = grupaTagowRepository.findAll().size();

        // Update the grupaTagow
        GrupaTagow updatedGrupaTagow = grupaTagowRepository.findById(grupaTagow.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGrupaTagow are not directly saved in db
        em.detach(updatedGrupaTagow);
        updatedGrupaTagow.nazwaGrupy(UPDATED_NAZWA_GRUPY);

        restGrupaTagowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGrupaTagow.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGrupaTagow))
            )
            .andExpect(status().isOk());

        // Validate the GrupaTagow in the database
        List<GrupaTagow> grupaTagowList = grupaTagowRepository.findAll();
        assertThat(grupaTagowList).hasSize(databaseSizeBeforeUpdate);
        GrupaTagow testGrupaTagow = grupaTagowList.get(grupaTagowList.size() - 1);
        assertThat(testGrupaTagow.getNazwaGrupy()).isEqualTo(UPDATED_NAZWA_GRUPY);
    }

    @Test
    @Transactional
    void putNonExistingGrupaTagow() throws Exception {
        int databaseSizeBeforeUpdate = grupaTagowRepository.findAll().size();
        grupaTagow.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGrupaTagowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, grupaTagow.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(grupaTagow))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupaTagow in the database
        List<GrupaTagow> grupaTagowList = grupaTagowRepository.findAll();
        assertThat(grupaTagowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGrupaTagow() throws Exception {
        int databaseSizeBeforeUpdate = grupaTagowRepository.findAll().size();
        grupaTagow.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupaTagowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(grupaTagow))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupaTagow in the database
        List<GrupaTagow> grupaTagowList = grupaTagowRepository.findAll();
        assertThat(grupaTagowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGrupaTagow() throws Exception {
        int databaseSizeBeforeUpdate = grupaTagowRepository.findAll().size();
        grupaTagow.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupaTagowMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupaTagow)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GrupaTagow in the database
        List<GrupaTagow> grupaTagowList = grupaTagowRepository.findAll();
        assertThat(grupaTagowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGrupaTagowWithPatch() throws Exception {
        // Initialize the database
        grupaTagowRepository.saveAndFlush(grupaTagow);

        int databaseSizeBeforeUpdate = grupaTagowRepository.findAll().size();

        // Update the grupaTagow using partial update
        GrupaTagow partialUpdatedGrupaTagow = new GrupaTagow();
        partialUpdatedGrupaTagow.setId(grupaTagow.getId());

        partialUpdatedGrupaTagow.nazwaGrupy(UPDATED_NAZWA_GRUPY);

        restGrupaTagowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrupaTagow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGrupaTagow))
            )
            .andExpect(status().isOk());

        // Validate the GrupaTagow in the database
        List<GrupaTagow> grupaTagowList = grupaTagowRepository.findAll();
        assertThat(grupaTagowList).hasSize(databaseSizeBeforeUpdate);
        GrupaTagow testGrupaTagow = grupaTagowList.get(grupaTagowList.size() - 1);
        assertThat(testGrupaTagow.getNazwaGrupy()).isEqualTo(UPDATED_NAZWA_GRUPY);
    }

    @Test
    @Transactional
    void fullUpdateGrupaTagowWithPatch() throws Exception {
        // Initialize the database
        grupaTagowRepository.saveAndFlush(grupaTagow);

        int databaseSizeBeforeUpdate = grupaTagowRepository.findAll().size();

        // Update the grupaTagow using partial update
        GrupaTagow partialUpdatedGrupaTagow = new GrupaTagow();
        partialUpdatedGrupaTagow.setId(grupaTagow.getId());

        partialUpdatedGrupaTagow.nazwaGrupy(UPDATED_NAZWA_GRUPY);

        restGrupaTagowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrupaTagow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGrupaTagow))
            )
            .andExpect(status().isOk());

        // Validate the GrupaTagow in the database
        List<GrupaTagow> grupaTagowList = grupaTagowRepository.findAll();
        assertThat(grupaTagowList).hasSize(databaseSizeBeforeUpdate);
        GrupaTagow testGrupaTagow = grupaTagowList.get(grupaTagowList.size() - 1);
        assertThat(testGrupaTagow.getNazwaGrupy()).isEqualTo(UPDATED_NAZWA_GRUPY);
    }

    @Test
    @Transactional
    void patchNonExistingGrupaTagow() throws Exception {
        int databaseSizeBeforeUpdate = grupaTagowRepository.findAll().size();
        grupaTagow.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGrupaTagowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, grupaTagow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(grupaTagow))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupaTagow in the database
        List<GrupaTagow> grupaTagowList = grupaTagowRepository.findAll();
        assertThat(grupaTagowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGrupaTagow() throws Exception {
        int databaseSizeBeforeUpdate = grupaTagowRepository.findAll().size();
        grupaTagow.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupaTagowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(grupaTagow))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupaTagow in the database
        List<GrupaTagow> grupaTagowList = grupaTagowRepository.findAll();
        assertThat(grupaTagowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGrupaTagow() throws Exception {
        int databaseSizeBeforeUpdate = grupaTagowRepository.findAll().size();
        grupaTagow.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupaTagowMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(grupaTagow))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GrupaTagow in the database
        List<GrupaTagow> grupaTagowList = grupaTagowRepository.findAll();
        assertThat(grupaTagowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGrupaTagow() throws Exception {
        // Initialize the database
        grupaTagowRepository.saveAndFlush(grupaTagow);

        int databaseSizeBeforeDelete = grupaTagowRepository.findAll().size();

        // Delete the grupaTagow
        restGrupaTagowMockMvc
            .perform(delete(ENTITY_API_URL_ID, grupaTagow.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GrupaTagow> grupaTagowList = grupaTagowRepository.findAll();
        assertThat(grupaTagowList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
