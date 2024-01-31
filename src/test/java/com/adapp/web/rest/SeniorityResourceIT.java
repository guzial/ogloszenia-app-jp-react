package com.adapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adapp.IntegrationTest;
import com.adapp.domain.Seniority;
import com.adapp.repository.SeniorityRepository;
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
 * Integration tests for the {@link SeniorityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeniorityResourceIT {

    private static final String DEFAULT_NAZWA = "AAAAAAAAAA";
    private static final String UPDATED_NAZWA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/seniorities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SeniorityRepository seniorityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeniorityMockMvc;

    private Seniority seniority;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seniority createEntity(EntityManager em) {
        Seniority seniority = new Seniority().nazwa(DEFAULT_NAZWA);
        return seniority;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seniority createUpdatedEntity(EntityManager em) {
        Seniority seniority = new Seniority().nazwa(UPDATED_NAZWA);
        return seniority;
    }

    @BeforeEach
    public void initTest() {
        seniority = createEntity(em);
    }

    @Test
    @Transactional
    void createSeniority() throws Exception {
        int databaseSizeBeforeCreate = seniorityRepository.findAll().size();
        // Create the Seniority
        restSeniorityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seniority)))
            .andExpect(status().isCreated());

        // Validate the Seniority in the database
        List<Seniority> seniorityList = seniorityRepository.findAll();
        assertThat(seniorityList).hasSize(databaseSizeBeforeCreate + 1);
        Seniority testSeniority = seniorityList.get(seniorityList.size() - 1);
        assertThat(testSeniority.getNazwa()).isEqualTo(DEFAULT_NAZWA);
    }

    @Test
    @Transactional
    void createSeniorityWithExistingId() throws Exception {
        // Create the Seniority with an existing ID
        seniority.setId(1L);

        int databaseSizeBeforeCreate = seniorityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeniorityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seniority)))
            .andExpect(status().isBadRequest());

        // Validate the Seniority in the database
        List<Seniority> seniorityList = seniorityRepository.findAll();
        assertThat(seniorityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSeniorities() throws Exception {
        // Initialize the database
        seniorityRepository.saveAndFlush(seniority);

        // Get all the seniorityList
        restSeniorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seniority.getId().intValue())))
            .andExpect(jsonPath("$.[*].nazwa").value(hasItem(DEFAULT_NAZWA)));
    }

    @Test
    @Transactional
    void getSeniority() throws Exception {
        // Initialize the database
        seniorityRepository.saveAndFlush(seniority);

        // Get the seniority
        restSeniorityMockMvc
            .perform(get(ENTITY_API_URL_ID, seniority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seniority.getId().intValue()))
            .andExpect(jsonPath("$.nazwa").value(DEFAULT_NAZWA));
    }

    @Test
    @Transactional
    void getNonExistingSeniority() throws Exception {
        // Get the seniority
        restSeniorityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeniority() throws Exception {
        // Initialize the database
        seniorityRepository.saveAndFlush(seniority);

        int databaseSizeBeforeUpdate = seniorityRepository.findAll().size();

        // Update the seniority
        Seniority updatedSeniority = seniorityRepository.findById(seniority.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSeniority are not directly saved in db
        em.detach(updatedSeniority);
        updatedSeniority.nazwa(UPDATED_NAZWA);

        restSeniorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSeniority.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSeniority))
            )
            .andExpect(status().isOk());

        // Validate the Seniority in the database
        List<Seniority> seniorityList = seniorityRepository.findAll();
        assertThat(seniorityList).hasSize(databaseSizeBeforeUpdate);
        Seniority testSeniority = seniorityList.get(seniorityList.size() - 1);
        assertThat(testSeniority.getNazwa()).isEqualTo(UPDATED_NAZWA);
    }

    @Test
    @Transactional
    void putNonExistingSeniority() throws Exception {
        int databaseSizeBeforeUpdate = seniorityRepository.findAll().size();
        seniority.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeniorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seniority.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seniority))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seniority in the database
        List<Seniority> seniorityList = seniorityRepository.findAll();
        assertThat(seniorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeniority() throws Exception {
        int databaseSizeBeforeUpdate = seniorityRepository.findAll().size();
        seniority.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeniorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seniority))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seniority in the database
        List<Seniority> seniorityList = seniorityRepository.findAll();
        assertThat(seniorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeniority() throws Exception {
        int databaseSizeBeforeUpdate = seniorityRepository.findAll().size();
        seniority.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeniorityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seniority)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seniority in the database
        List<Seniority> seniorityList = seniorityRepository.findAll();
        assertThat(seniorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeniorityWithPatch() throws Exception {
        // Initialize the database
        seniorityRepository.saveAndFlush(seniority);

        int databaseSizeBeforeUpdate = seniorityRepository.findAll().size();

        // Update the seniority using partial update
        Seniority partialUpdatedSeniority = new Seniority();
        partialUpdatedSeniority.setId(seniority.getId());

        partialUpdatedSeniority.nazwa(UPDATED_NAZWA);

        restSeniorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeniority.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeniority))
            )
            .andExpect(status().isOk());

        // Validate the Seniority in the database
        List<Seniority> seniorityList = seniorityRepository.findAll();
        assertThat(seniorityList).hasSize(databaseSizeBeforeUpdate);
        Seniority testSeniority = seniorityList.get(seniorityList.size() - 1);
        assertThat(testSeniority.getNazwa()).isEqualTo(UPDATED_NAZWA);
    }

    @Test
    @Transactional
    void fullUpdateSeniorityWithPatch() throws Exception {
        // Initialize the database
        seniorityRepository.saveAndFlush(seniority);

        int databaseSizeBeforeUpdate = seniorityRepository.findAll().size();

        // Update the seniority using partial update
        Seniority partialUpdatedSeniority = new Seniority();
        partialUpdatedSeniority.setId(seniority.getId());

        partialUpdatedSeniority.nazwa(UPDATED_NAZWA);

        restSeniorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeniority.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeniority))
            )
            .andExpect(status().isOk());

        // Validate the Seniority in the database
        List<Seniority> seniorityList = seniorityRepository.findAll();
        assertThat(seniorityList).hasSize(databaseSizeBeforeUpdate);
        Seniority testSeniority = seniorityList.get(seniorityList.size() - 1);
        assertThat(testSeniority.getNazwa()).isEqualTo(UPDATED_NAZWA);
    }

    @Test
    @Transactional
    void patchNonExistingSeniority() throws Exception {
        int databaseSizeBeforeUpdate = seniorityRepository.findAll().size();
        seniority.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeniorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seniority.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seniority))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seniority in the database
        List<Seniority> seniorityList = seniorityRepository.findAll();
        assertThat(seniorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeniority() throws Exception {
        int databaseSizeBeforeUpdate = seniorityRepository.findAll().size();
        seniority.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeniorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seniority))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seniority in the database
        List<Seniority> seniorityList = seniorityRepository.findAll();
        assertThat(seniorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeniority() throws Exception {
        int databaseSizeBeforeUpdate = seniorityRepository.findAll().size();
        seniority.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeniorityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(seniority))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seniority in the database
        List<Seniority> seniorityList = seniorityRepository.findAll();
        assertThat(seniorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeniority() throws Exception {
        // Initialize the database
        seniorityRepository.saveAndFlush(seniority);

        int databaseSizeBeforeDelete = seniorityRepository.findAll().size();

        // Delete the seniority
        restSeniorityMockMvc
            .perform(delete(ENTITY_API_URL_ID, seniority.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Seniority> seniorityList = seniorityRepository.findAll();
        assertThat(seniorityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
