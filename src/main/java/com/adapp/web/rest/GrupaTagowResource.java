package com.adapp.web.rest;

import com.adapp.domain.GrupaTagow;
import com.adapp.repository.GrupaTagowRepository;
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
 * REST controller for managing {@link com.adapp.domain.GrupaTagow}.
 */
@RestController
@RequestMapping("/api/grupa-tagows")
@Transactional
public class GrupaTagowResource {

    private final Logger log = LoggerFactory.getLogger(GrupaTagowResource.class);

    private static final String ENTITY_NAME = "grupaTagow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GrupaTagowRepository grupaTagowRepository;

    public GrupaTagowResource(GrupaTagowRepository grupaTagowRepository) {
        this.grupaTagowRepository = grupaTagowRepository;
    }

    /**
     * {@code POST  /grupa-tagows} : Create a new grupaTagow.
     *
     * @param grupaTagow the grupaTagow to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new grupaTagow, or with status {@code 400 (Bad Request)} if the grupaTagow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GrupaTagow> createGrupaTagow(@RequestBody GrupaTagow grupaTagow) throws URISyntaxException {
        log.debug("REST request to save GrupaTagow : {}", grupaTagow);
        if (grupaTagow.getId() != null) {
            throw new BadRequestAlertException("A new grupaTagow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GrupaTagow result = grupaTagowRepository.save(grupaTagow);
        return ResponseEntity
            .created(new URI("/api/grupa-tagows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /grupa-tagows/:id} : Updates an existing grupaTagow.
     *
     * @param id the id of the grupaTagow to save.
     * @param grupaTagow the grupaTagow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupaTagow,
     * or with status {@code 400 (Bad Request)} if the grupaTagow is not valid,
     * or with status {@code 500 (Internal Server Error)} if the grupaTagow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GrupaTagow> updateGrupaTagow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GrupaTagow grupaTagow
    ) throws URISyntaxException {
        log.debug("REST request to update GrupaTagow : {}, {}", id, grupaTagow);
        if (grupaTagow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupaTagow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!grupaTagowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GrupaTagow result = grupaTagowRepository.save(grupaTagow);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, grupaTagow.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /grupa-tagows/:id} : Partial updates given fields of an existing grupaTagow, field will ignore if it is null
     *
     * @param id the id of the grupaTagow to save.
     * @param grupaTagow the grupaTagow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupaTagow,
     * or with status {@code 400 (Bad Request)} if the grupaTagow is not valid,
     * or with status {@code 404 (Not Found)} if the grupaTagow is not found,
     * or with status {@code 500 (Internal Server Error)} if the grupaTagow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GrupaTagow> partialUpdateGrupaTagow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GrupaTagow grupaTagow
    ) throws URISyntaxException {
        log.debug("REST request to partial update GrupaTagow partially : {}, {}", id, grupaTagow);
        if (grupaTagow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupaTagow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!grupaTagowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GrupaTagow> result = grupaTagowRepository
            .findById(grupaTagow.getId())
            .map(existingGrupaTagow -> {
                if (grupaTagow.getNazwaGrupy() != null) {
                    existingGrupaTagow.setNazwaGrupy(grupaTagow.getNazwaGrupy());
                }

                return existingGrupaTagow;
            })
            .map(grupaTagowRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, grupaTagow.getId().toString())
        );
    }

    /**
     * {@code GET  /grupa-tagows} : get all the grupaTagows.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of grupaTagows in body.
     */
    @GetMapping("")
    public List<GrupaTagow> getAllGrupaTagows() {
        log.debug("REST request to get all GrupaTagows");
        return grupaTagowRepository.findAll();
    }

    /**
     * {@code GET  /grupa-tagows/:id} : get the "id" grupaTagow.
     *
     * @param id the id of the grupaTagow to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the grupaTagow, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GrupaTagow> getGrupaTagow(@PathVariable("id") Long id) {
        log.debug("REST request to get GrupaTagow : {}", id);
        Optional<GrupaTagow> grupaTagow = grupaTagowRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(grupaTagow);
    }

    /**
     * {@code DELETE  /grupa-tagows/:id} : delete the "id" grupaTagow.
     *
     * @param id the id of the grupaTagow to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrupaTagow(@PathVariable("id") Long id) {
        log.debug("REST request to delete GrupaTagow : {}", id);
        grupaTagowRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
