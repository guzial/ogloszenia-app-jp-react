package com.adapp.web.rest;

import com.adapp.domain.Ogloszenie;
import com.adapp.repository.OgloszenieRepository;
import com.adapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.adapp.domain.Ogloszenie}.
 */
@RestController
@RequestMapping("/api/ogloszenies")
@Transactional
public class OgloszenieResource {

    private final Logger log = LoggerFactory.getLogger(OgloszenieResource.class);

    private static final String ENTITY_NAME = "ogloszenie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OgloszenieRepository ogloszenieRepository;

    public OgloszenieResource(OgloszenieRepository ogloszenieRepository) {
        this.ogloszenieRepository = ogloszenieRepository;
    }

    /**
     * {@code POST  /ogloszenies} : Create a new ogloszenie.
     *
     * @param ogloszenie the ogloszenie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ogloszenie, or with status {@code 400 (Bad Request)} if the ogloszenie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Ogloszenie> createOgloszenie(@RequestBody Ogloszenie ogloszenie) throws URISyntaxException {
        log.debug("REST request to save Ogloszenie : {}", ogloszenie);
        if (ogloszenie.getId() != null) {
            throw new BadRequestAlertException("A new ogloszenie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ogloszenie result = ogloszenieRepository.save(ogloszenie);
        return ResponseEntity
            .created(new URI("/api/ogloszenies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ogloszenies/:id} : Updates an existing ogloszenie.
     *
     * @param id the id of the ogloszenie to save.
     * @param ogloszenie the ogloszenie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ogloszenie,
     * or with status {@code 400 (Bad Request)} if the ogloszenie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ogloszenie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ogloszenie> updateOgloszenie(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ogloszenie ogloszenie
    ) throws URISyntaxException {
        log.debug("REST request to update Ogloszenie : {}, {}", id, ogloszenie);
        if (ogloszenie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ogloszenie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ogloszenieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ogloszenie result = ogloszenieRepository.save(ogloszenie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ogloszenie.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ogloszenies/:id} : Partial updates given fields of an existing ogloszenie, field will ignore if it is null
     *
     * @param id the id of the ogloszenie to save.
     * @param ogloszenie the ogloszenie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ogloszenie,
     * or with status {@code 400 (Bad Request)} if the ogloszenie is not valid,
     * or with status {@code 404 (Not Found)} if the ogloszenie is not found,
     * or with status {@code 500 (Internal Server Error)} if the ogloszenie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ogloszenie> partialUpdateOgloszenie(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ogloszenie ogloszenie
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ogloszenie partially : {}, {}", id, ogloszenie);
        if (ogloszenie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ogloszenie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ogloszenieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ogloszenie> result = ogloszenieRepository
            .findById(ogloszenie.getId())
            .map(existingOgloszenie -> {
                if (ogloszenie.getTytul() != null) {
                    existingOgloszenie.setTytul(ogloszenie.getTytul());
                }
                if (ogloszenie.getOpis() != null) {
                    existingOgloszenie.setOpis(ogloszenie.getOpis());
                }
                if (ogloszenie.getDataPublikacji() != null) {
                    existingOgloszenie.setDataPublikacji(ogloszenie.getDataPublikacji());
                }
                if (ogloszenie.getDataWaznosci() != null) {
                    existingOgloszenie.setDataWaznosci(ogloszenie.getDataWaznosci());
                }
                if (ogloszenie.getStartOd() != null) {
                    existingOgloszenie.setStartOd(ogloszenie.getStartOd());
                }
                if (ogloszenie.getCzyWidelki() != null) {
                    existingOgloszenie.setCzyWidelki(ogloszenie.getCzyWidelki());
                }
                if (ogloszenie.getWidelkiMin() != null) {
                    existingOgloszenie.setWidelkiMin(ogloszenie.getWidelkiMin());
                }
                if (ogloszenie.getWidelkiMax() != null) {
                    existingOgloszenie.setWidelkiMax(ogloszenie.getWidelkiMax());
                }
                if (ogloszenie.getAktywne() != null) {
                    existingOgloszenie.setAktywne(ogloszenie.getAktywne());
                }

                return existingOgloszenie;
            })
            .map(ogloszenieRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ogloszenie.getId().toString())
        );
    }

    /**
     * {@code GET  /ogloszenies} : get all the ogloszenies.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ogloszenies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Ogloszenie>> getAllOgloszenies(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Ogloszenies");
        Page<Ogloszenie> page;
        if (eagerload) {
            page = ogloszenieRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = ogloszenieRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ogloszenies/:id} : get the "id" ogloszenie.
     *
     * @param id the id of the ogloszenie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ogloszenie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ogloszenie> getOgloszenie(@PathVariable("id") Long id) {
        log.debug("REST request to get Ogloszenie : {}", id);
        Optional<Ogloszenie> ogloszenie = ogloszenieRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(ogloszenie);
    }

    /**
     * {@code DELETE  /ogloszenies/:id} : delete the "id" ogloszenie.
     *
     * @param id the id of the ogloszenie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOgloszenie(@PathVariable("id") Long id) {
        log.debug("REST request to delete Ogloszenie : {}", id);
        ogloszenieRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
