package com.adapp.web.rest;

import com.adapp.domain.Seniority;
import com.adapp.repository.SeniorityRepository;
import com.adapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.adapp.domain.Seniority}.
 */
@RestController
@RequestMapping("/api/seniorities")
@Transactional
public class SeniorityResource {

    private final Logger log = LoggerFactory.getLogger(SeniorityResource.class);

    private static final String ENTITY_NAME = "seniority";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SeniorityRepository seniorityRepository;

    public SeniorityResource(SeniorityRepository seniorityRepository) {
        this.seniorityRepository = seniorityRepository;
    }

    /**
     * {@code POST  /seniorities} : Create a new seniority.
     *
     * @param seniority the seniority to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new seniority, or with status {@code 400 (Bad Request)} if the seniority has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Seniority> createSeniority(@RequestBody Seniority seniority) throws URISyntaxException {
        log.debug("REST request to save Seniority : {}", seniority);
        if (seniority.getId() != null) {
            throw new BadRequestAlertException("A new seniority cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Seniority result = seniorityRepository.save(seniority);
        return ResponseEntity
            .created(new URI("/api/seniorities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /seniorities/:id} : Updates an existing seniority.
     *
     * @param id the id of the seniority to save.
     * @param seniority the seniority to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seniority,
     * or with status {@code 400 (Bad Request)} if the seniority is not valid,
     * or with status {@code 500 (Internal Server Error)} if the seniority couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Seniority> updateSeniority(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Seniority seniority
    ) throws URISyntaxException {
        log.debug("REST request to update Seniority : {}, {}", id, seniority);
        if (seniority.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seniority.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seniorityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Seniority result = seniorityRepository.save(seniority);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, seniority.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /seniorities/:id} : Partial updates given fields of an existing seniority, field will ignore if it is null
     *
     * @param id the id of the seniority to save.
     * @param seniority the seniority to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seniority,
     * or with status {@code 400 (Bad Request)} if the seniority is not valid,
     * or with status {@code 404 (Not Found)} if the seniority is not found,
     * or with status {@code 500 (Internal Server Error)} if the seniority couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Seniority> partialUpdateSeniority(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Seniority seniority
    ) throws URISyntaxException {
        log.debug("REST request to partial update Seniority partially : {}, {}", id, seniority);
        if (seniority.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seniority.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seniorityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Seniority> result = seniorityRepository
            .findById(seniority.getId())
            .map(existingSeniority -> {
                if (seniority.getNazwa() != null) {
                    existingSeniority.setNazwa(seniority.getNazwa());
                }

                return existingSeniority;
            })
            .map(seniorityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, seniority.getId().toString())
        );
    }

    /**
     * {@code GET  /seniorities} : get all the seniorities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of seniorities in body.
     */
    @GetMapping("")
    public List<Seniority> getAllSeniorities() {
        log.debug("REST request to get all Seniorities");
        return seniorityRepository.findAll();
    }

    /**
     * {@code GET  /seniorities/:id} : get the "id" seniority.
     *
     * @param id the id of the seniority to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the seniority, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Seniority> getSeniority(@PathVariable("id") Long id) {
        log.debug("REST request to get Seniority : {}", id);
        Optional<Seniority> seniority = seniorityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(seniority);
    }

    /**
     * {@code DELETE  /seniorities/:id} : delete the "id" seniority.
     *
     * @param id the id of the seniority to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeniority(@PathVariable("id") Long id) {
        log.debug("REST request to delete Seniority : {}", id);
        seniorityRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
