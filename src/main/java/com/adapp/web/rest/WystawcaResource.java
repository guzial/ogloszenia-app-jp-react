package com.adapp.web.rest;

import com.adapp.domain.Wystawca;
import com.adapp.repository.WystawcaRepository;
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
 * REST controller for managing {@link com.adapp.domain.Wystawca}.
 */
@RestController
@RequestMapping("/api/wystawcas")
@Transactional
public class WystawcaResource {

    private final Logger log = LoggerFactory.getLogger(WystawcaResource.class);

    private static final String ENTITY_NAME = "wystawca";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WystawcaRepository wystawcaRepository;

    public WystawcaResource(WystawcaRepository wystawcaRepository) {
        this.wystawcaRepository = wystawcaRepository;
    }

    /**
     * {@code POST  /wystawcas} : Create a new wystawca.
     *
     * @param wystawca the wystawca to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wystawca, or with status {@code 400 (Bad Request)} if the wystawca has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Wystawca> createWystawca(@RequestBody Wystawca wystawca) throws URISyntaxException {
        log.debug("REST request to save Wystawca : {}", wystawca);
        if (wystawca.getId() != null) {
            throw new BadRequestAlertException("A new wystawca cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Wystawca result = wystawcaRepository.save(wystawca);
        return ResponseEntity
            .created(new URI("/api/wystawcas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wystawcas/:id} : Updates an existing wystawca.
     *
     * @param id the id of the wystawca to save.
     * @param wystawca the wystawca to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wystawca,
     * or with status {@code 400 (Bad Request)} if the wystawca is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wystawca couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Wystawca> updateWystawca(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Wystawca wystawca
    ) throws URISyntaxException {
        log.debug("REST request to update Wystawca : {}, {}", id, wystawca);
        if (wystawca.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wystawca.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wystawcaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Wystawca result = wystawcaRepository.save(wystawca);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wystawca.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wystawcas/:id} : Partial updates given fields of an existing wystawca, field will ignore if it is null
     *
     * @param id the id of the wystawca to save.
     * @param wystawca the wystawca to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wystawca,
     * or with status {@code 400 (Bad Request)} if the wystawca is not valid,
     * or with status {@code 404 (Not Found)} if the wystawca is not found,
     * or with status {@code 500 (Internal Server Error)} if the wystawca couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Wystawca> partialUpdateWystawca(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Wystawca wystawca
    ) throws URISyntaxException {
        log.debug("REST request to partial update Wystawca partially : {}, {}", id, wystawca);
        if (wystawca.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wystawca.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wystawcaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Wystawca> result = wystawcaRepository
            .findById(wystawca.getId())
            .map(existingWystawca -> {
                if (wystawca.getNazwa() != null) {
                    existingWystawca.setNazwa(wystawca.getNazwa());
                }
                if (wystawca.getKontakt() != null) {
                    existingWystawca.setKontakt(wystawca.getKontakt());
                }

                return existingWystawca;
            })
            .map(wystawcaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wystawca.getId().toString())
        );
    }

    /**
     * {@code GET  /wystawcas} : get all the wystawcas.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wystawcas in body.
     */
    @GetMapping("")
    public List<Wystawca> getAllWystawcas(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Wystawcas");
        if (eagerload) {
            return wystawcaRepository.findAllWithEagerRelationships();
        } else {
            return wystawcaRepository.findAll();
        }
    }

    /**
     * {@code GET  /wystawcas/:id} : get the "id" wystawca.
     *
     * @param id the id of the wystawca to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wystawca, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Wystawca> getWystawca(@PathVariable("id") Long id) {
        log.debug("REST request to get Wystawca : {}", id);
        Optional<Wystawca> wystawca = wystawcaRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(wystawca);
    }

    /**
     * {@code DELETE  /wystawcas/:id} : delete the "id" wystawca.
     *
     * @param id the id of the wystawca to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWystawca(@PathVariable("id") Long id) {
        log.debug("REST request to delete Wystawca : {}", id);
        wystawcaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
