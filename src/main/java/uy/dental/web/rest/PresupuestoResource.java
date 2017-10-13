package uy.dental.web.rest;

import com.codahale.metrics.annotation.Timed;
import uy.dental.domain.Presupuesto;

import uy.dental.repository.PresupuestoRepository;
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
 * REST controller for managing Presupuesto.
 */
@RestController
@RequestMapping("/api")
public class PresupuestoResource {

    private final Logger log = LoggerFactory.getLogger(PresupuestoResource.class);

    private static final String ENTITY_NAME = "presupuesto";

    private final PresupuestoRepository presupuestoRepository;

    public PresupuestoResource(PresupuestoRepository presupuestoRepository) {
        this.presupuestoRepository = presupuestoRepository;
    }

    /**
     * POST  /presupuestos : Create a new presupuesto.
     *
     * @param presupuesto the presupuesto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new presupuesto, or with status 400 (Bad Request) if the presupuesto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/presupuestos")
    @Timed
    public ResponseEntity<Presupuesto> createPresupuesto(@Valid @RequestBody Presupuesto presupuesto) throws URISyntaxException {
        log.debug("REST request to save Presupuesto : {}", presupuesto);
        if (presupuesto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new presupuesto cannot already have an ID")).body(null);
        }
        Presupuesto result = presupuestoRepository.save(presupuesto);
        return ResponseEntity.created(new URI("/api/presupuestos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /presupuestos : Updates an existing presupuesto.
     *
     * @param presupuesto the presupuesto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated presupuesto,
     * or with status 400 (Bad Request) if the presupuesto is not valid,
     * or with status 500 (Internal Server Error) if the presupuesto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/presupuestos")
    @Timed
    public ResponseEntity<Presupuesto> updatePresupuesto(@Valid @RequestBody Presupuesto presupuesto) throws URISyntaxException {
        log.debug("REST request to update Presupuesto : {}", presupuesto);
        if (presupuesto.getId() == null) {
            return createPresupuesto(presupuesto);
        }
        Presupuesto result = presupuestoRepository.save(presupuesto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, presupuesto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /presupuestos : get all the presupuestos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of presupuestos in body
     */
    @GetMapping("/presupuestos")
    @Timed
    public ResponseEntity<List<Presupuesto>> getAllPresupuestos(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Presupuestos");
        Page<Presupuesto> page = presupuestoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/presupuestos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /presupuestos/:id : get the "id" presupuesto.
     *
     * @param id the id of the presupuesto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the presupuesto, or with status 404 (Not Found)
     */
    @GetMapping("/presupuestos/{id}")
    @Timed
    public ResponseEntity<Presupuesto> getPresupuesto(@PathVariable Long id) {
        log.debug("REST request to get Presupuesto : {}", id);
        Presupuesto presupuesto = presupuestoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(presupuesto));
    }

    /**
     * DELETE  /presupuestos/:id : delete the "id" presupuesto.
     *
     * @param id the id of the presupuesto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/presupuestos/{id}")
    @Timed
    public ResponseEntity<Void> deletePresupuesto(@PathVariable Long id) {
        log.debug("REST request to delete Presupuesto : {}", id);
        presupuestoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
