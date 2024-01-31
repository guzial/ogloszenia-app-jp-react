package com.adapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adapp.IntegrationTest;
import com.adapp.domain.TypUmowy;
import com.adapp.repository.TypUmowyRepository;
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
 * Integration tests for the {@link TypUmowyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypUmowyResourceIT {

    private static final String DEFAULT_TEKST = "AAAAAAAAAA";
    private static final String UPDATED_TEKST = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/typ-umowies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypUmowyRepository typUmowyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypUmowyMockMvc;

    private TypUmowy typUmowy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypUmowy createEntity(EntityManager em) {
        TypUmowy typUmowy = new TypUmowy().tekst(DEFAULT_TEKST);
        return typUmowy;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypUmowy createUpdatedEntity(EntityManager em) {
        TypUmowy typUmowy = new TypUmowy().tekst(UPDATED_TEKST);
        return typUmowy;
    }

    @BeforeEach
    public void initTest() {
        typUmowy = createEntity(em);
    }

    @Test
    @Transactional
    void createTypUmowy() throws Exception {
        int databaseSizeBeforeCreate = typUmowyRepository.findAll().size();
        // Create the TypUmowy
        restTypUmowyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typUmowy)))
            .andExpect(status().isCreated());

        // Validate the TypUmowy in the database
        List<TypUmowy> typUmowyList = typUmowyRepository.findAll();
        assertThat(typUmowyList).hasSize(databaseSizeBeforeCreate + 1);
        TypUmowy testTypUmowy = typUmowyList.get(typUmowyList.size() - 1);
        assertThat(testTypUmowy.getTekst()).isEqualTo(DEFAULT_TEKST);
    }

    @Test
    @Transactional
    void createTypUmowyWithExistingId() throws Exception {
        // Create the TypUmowy with an existing ID
        typUmowy.setId(1L);

        int databaseSizeBeforeCreate = typUmowyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypUmowyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typUmowy)))
            .andExpect(status().isBadRequest());

        // Validate the TypUmowy in the database
        List<TypUmowy> typUmowyList = typUmowyRepository.findAll();
        assertThat(typUmowyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypUmowies() throws Exception {
        // Initialize the database
        typUmowyRepository.saveAndFlush(typUmowy);

        // Get all the typUmowyList
        restTypUmowyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typUmowy.getId().intValue())))
            .andExpect(jsonPath("$.[*].tekst").value(hasItem(DEFAULT_TEKST)));
    }

    @Test
    @Transactional
    void getTypUmowy() throws Exception {
        // Initialize the database
        typUmowyRepository.saveAndFlush(typUmowy);

        // Get the typUmowy
        restTypUmowyMockMvc
            .perform(get(ENTITY_API_URL_ID, typUmowy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typUmowy.getId().intValue()))
            .andExpect(jsonPath("$.tekst").value(DEFAULT_TEKST));
    }

    @Test
    @Transactional
    void getNonExistingTypUmowy() throws Exception {
        // Get the typUmowy
        restTypUmowyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypUmowy() throws Exception {
        // Initialize the database
        typUmowyRepository.saveAndFlush(typUmowy);

        int databaseSizeBeforeUpdate = typUmowyRepository.findAll().size();

        // Update the typUmowy
        TypUmowy updatedTypUmowy = typUmowyRepository.findById(typUmowy.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypUmowy are not directly saved in db
        em.detach(updatedTypUmowy);
        updatedTypUmowy.tekst(UPDATED_TEKST);

        restTypUmowyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTypUmowy.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTypUmowy))
            )
            .andExpect(status().isOk());

        // Validate the TypUmowy in the database
        List<TypUmowy> typUmowyList = typUmowyRepository.findAll();
        assertThat(typUmowyList).hasSize(databaseSizeBeforeUpdate);
        TypUmowy testTypUmowy = typUmowyList.get(typUmowyList.size() - 1);
        assertThat(testTypUmowy.getTekst()).isEqualTo(UPDATED_TEKST);
    }

    @Test
    @Transactional
    void putNonExistingTypUmowy() throws Exception {
        int databaseSizeBeforeUpdate = typUmowyRepository.findAll().size();
        typUmowy.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypUmowyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typUmowy.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typUmowy))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypUmowy in the database
        List<TypUmowy> typUmowyList = typUmowyRepository.findAll();
        assertThat(typUmowyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypUmowy() throws Exception {
        int databaseSizeBeforeUpdate = typUmowyRepository.findAll().size();
        typUmowy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypUmowyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typUmowy))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypUmowy in the database
        List<TypUmowy> typUmowyList = typUmowyRepository.findAll();
        assertThat(typUmowyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypUmowy() throws Exception {
        int databaseSizeBeforeUpdate = typUmowyRepository.findAll().size();
        typUmowy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypUmowyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typUmowy)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypUmowy in the database
        List<TypUmowy> typUmowyList = typUmowyRepository.findAll();
        assertThat(typUmowyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypUmowyWithPatch() throws Exception {
        // Initialize the database
        typUmowyRepository.saveAndFlush(typUmowy);

        int databaseSizeBeforeUpdate = typUmowyRepository.findAll().size();

        // Update the typUmowy using partial update
        TypUmowy partialUpdatedTypUmowy = new TypUmowy();
        partialUpdatedTypUmowy.setId(typUmowy.getId());

        restTypUmowyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypUmowy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypUmowy))
            )
            .andExpect(status().isOk());

        // Validate the TypUmowy in the database
        List<TypUmowy> typUmowyList = typUmowyRepository.findAll();
        assertThat(typUmowyList).hasSize(databaseSizeBeforeUpdate);
        TypUmowy testTypUmowy = typUmowyList.get(typUmowyList.size() - 1);
        assertThat(testTypUmowy.getTekst()).isEqualTo(DEFAULT_TEKST);
    }

    @Test
    @Transactional
    void fullUpdateTypUmowyWithPatch() throws Exception {
        // Initialize the database
        typUmowyRepository.saveAndFlush(typUmowy);

        int databaseSizeBeforeUpdate = typUmowyRepository.findAll().size();

        // Update the typUmowy using partial update
        TypUmowy partialUpdatedTypUmowy = new TypUmowy();
        partialUpdatedTypUmowy.setId(typUmowy.getId());

        partialUpdatedTypUmowy.tekst(UPDATED_TEKST);

        restTypUmowyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypUmowy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypUmowy))
            )
            .andExpect(status().isOk());

        // Validate the TypUmowy in the database
        List<TypUmowy> typUmowyList = typUmowyRepository.findAll();
        assertThat(typUmowyList).hasSize(databaseSizeBeforeUpdate);
        TypUmowy testTypUmowy = typUmowyList.get(typUmowyList.size() - 1);
        assertThat(testTypUmowy.getTekst()).isEqualTo(UPDATED_TEKST);
    }

    @Test
    @Transactional
    void patchNonExistingTypUmowy() throws Exception {
        int databaseSizeBeforeUpdate = typUmowyRepository.findAll().size();
        typUmowy.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypUmowyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typUmowy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typUmowy))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypUmowy in the database
        List<TypUmowy> typUmowyList = typUmowyRepository.findAll();
        assertThat(typUmowyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypUmowy() throws Exception {
        int databaseSizeBeforeUpdate = typUmowyRepository.findAll().size();
        typUmowy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypUmowyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typUmowy))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypUmowy in the database
        List<TypUmowy> typUmowyList = typUmowyRepository.findAll();
        assertThat(typUmowyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypUmowy() throws Exception {
        int databaseSizeBeforeUpdate = typUmowyRepository.findAll().size();
        typUmowy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypUmowyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typUmowy)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypUmowy in the database
        List<TypUmowy> typUmowyList = typUmowyRepository.findAll();
        assertThat(typUmowyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypUmowy() throws Exception {
        // Initialize the database
        typUmowyRepository.saveAndFlush(typUmowy);

        int databaseSizeBeforeDelete = typUmowyRepository.findAll().size();

        // Delete the typUmowy
        restTypUmowyMockMvc
            .perform(delete(ENTITY_API_URL_ID, typUmowy.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypUmowy> typUmowyList = typUmowyRepository.findAll();
        assertThat(typUmowyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
