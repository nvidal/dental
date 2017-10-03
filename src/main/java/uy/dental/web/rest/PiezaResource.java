package uy.dental.web.rest;

import com.codahale.metrics.annotation.Timed;
import uy.dental.domain.Pieza;

import uy.dental.repository.PiezaRepository;
import uy.dental.web.rest.util.HeaderUtil;
import uy.dental.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Pieza.
 */
@RestController
@RequestMapping("/api")
public class PiezaResource {

    private final Logger log = LoggerFactory.getLogger(PiezaResource.class);

    private static final String ENTITY_NAME = "pieza";

    private final PiezaRepository piezaRepository;

    public PiezaResource(PiezaRepository piezaRepository) {
        this.piezaRepository = piezaRepository;
    }

    /**
     * POST  /piezas : Create a new pieza.
     *
     * @param pieza the pieza to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pieza, or with status 400 (Bad Request) if the pieza has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/piezas")
    @Timed
    public ResponseEntity<Pieza> createPieza(@Valid @RequestBody Pieza pieza) throws URISyntaxException {
        log.debug("REST request to save Pieza : {}", pieza);
        if (pieza.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pieza cannot already have an ID")).body(null);
        }
        Pieza result = piezaRepository.save(pieza);
        return ResponseEntity.created(new URI("/api/piezas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /piezas : Updates an existing pieza.
     *
     * @param pieza the pieza to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pieza,
     * or with status 400 (Bad Request) if the pieza is not valid,
     * or with status 500 (Internal Server Error) if the pieza couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/piezas")
    @Timed
    public ResponseEntity<Pieza> updatePieza(@Valid @RequestBody Pieza pieza) throws URISyntaxException {
        log.debug("REST request to update Pieza : {}", pieza);
        if (pieza.getId() == null) {
            return createPieza(pieza);
        }
        Pieza result = piezaRepository.save(pieza);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pieza.getId().toString()))
            .body(result);
    }

    /**
     * GET  /piezas : get all the piezas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of piezas in body
     */
    @GetMapping("/piezas")
    @Timed
    public ResponseEntity<List<Pieza>> getAllPiezas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Piezas");
        Page<Pieza> page = piezaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/piezas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /piezas/:id : get the "id" pieza.
     *
     * @param id the id of the pieza to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pieza, or with status 404 (Not Found)
     */
    @GetMapping("/piezas/{id}")
    @Timed
    public ResponseEntity<Pieza> getPieza(@PathVariable Long id) {
        log.debug("REST request to get Pieza : {}", id);
        Pieza pieza = piezaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pieza));
    }

    /**
     * DELETE  /piezas/:id : delete the "id" pieza.
     *
     * @param id the id of the pieza to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/piezas/{id}")
    @Timed
    public ResponseEntity<Void> deletePieza(@PathVariable Long id) {
        log.debug("REST request to delete Pieza : {}", id);
        piezaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
