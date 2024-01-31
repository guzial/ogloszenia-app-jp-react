package com.adapp.web.rest;

import com.adapp.domain.TypUmowy;
import com.adapp.repository.TypUmowyRepository;
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
 * REST controller for managing {@link com.adapp.domain.TypUmowy}.
 */
@RestController
@RequestMapping("/api/typ-umowies")
@Transactional
public class TypUmowyResource {

    private final Logger log = LoggerFactory.getLogger(TypUmowyResource.class);

    private static final String ENTITY_NAME = "typUmowy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypUmowyRepository typUmowyRepository;

    public TypUmowyResource(TypUmowyRepository typUmowyRepository) {
        this.typUmowyRepository = typUmowyRepository;
    }

    /**
     * {@code POST  /typ-umowies} : Create a new typUmowy.
     *
     * @param typUmowy the typUmowy to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typUmowy, or with status {@code 400 (Bad Request)} if the typUmowy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypUmowy> createTypUmowy(@RequestBody TypUmowy typUmowy) throws URISyntaxException {
        log.debug("REST request to save TypUmowy : {}", typUmowy);
        if (typUmowy.getId() != null) {
            throw new BadRequestAlertException("A new typUmowy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypUmowy result = typUmowyRepository.save(typUmowy);
        return ResponseEntity
            .created(new URI("/api/typ-umowies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /typ-umowies/:id} : Updates an existing typUmowy.
     *
     * @param id the id of the typUmowy to save.
     * @param typUmowy the typUmowy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typUmowy,
     * or with status {@code 400 (Bad Request)} if the typUmowy is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typUmowy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypUmowy> updateTypUmowy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypUmowy typUmowy
    ) throws URISyntaxException {
        log.debug("REST request to update TypUmowy : {}, {}", id, typUmowy);
        if (typUmowy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typUmowy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typUmowyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypUmowy result = typUmowyRepository.save(typUmowy);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typUmowy.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /typ-umowies/:id} : Partial updates given fields of an existing typUmowy, field will ignore if it is null
     *
     * @param id the id of the typUmowy to save.
     * @param typUmowy the typUmowy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typUmowy,
     * or with status {@code 400 (Bad Request)} if the typUmowy is not valid,
     * or with status {@code 404 (Not Found)} if the typUmowy is not found,
     * or with status {@code 500 (Internal Server Error)} if the typUmowy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypUmowy> partialUpdateTypUmowy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypUmowy typUmowy
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypUmowy partially : {}, {}", id, typUmowy);
        if (typUmowy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typUmowy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typUmowyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypUmowy> result = typUmowyRepository
            .findById(typUmowy.getId())
            .map(existingTypUmowy -> {
                if (typUmowy.getTekst() != null) {
                    existingTypUmowy.setTekst(typUmowy.getTekst());
                }

                return existingTypUmowy;
            })
            .map(typUmowyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typUmowy.getId().toString())
        );
    }

    /**
     * {@code GET  /typ-umowies} : get all the typUmowies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typUmowies in body.
     */
    @GetMapping("")
    public List<TypUmowy> getAllTypUmowies() {
        log.debug("REST request to get all TypUmowies");
        return typUmowyRepository.findAll();
    }

    /**
     * {@code GET  /typ-umowies/:id} : get the "id" typUmowy.
     *
     * @param id the id of the typUmowy to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typUmowy, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypUmowy> getTypUmowy(@PathVariable("id") Long id) {
        log.debug("REST request to get TypUmowy : {}", id);
        Optional<TypUmowy> typUmowy = typUmowyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(typUmowy);
    }

    /**
     * {@code DELETE  /typ-umowies/:id} : delete the "id" typUmowy.
     *
     * @param id the id of the typUmowy to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypUmowy(@PathVariable("id") Long id) {
        log.debug("REST request to delete TypUmowy : {}", id);
        typUmowyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
