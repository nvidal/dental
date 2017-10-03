package uy.dental.web.rest;

import com.codahale.metrics.annotation.Timed;
import uy.dental.domain.Procedimiento;

import uy.dental.repository.ProcedimientoRepository;
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
 * REST controller for managing Procedimiento.
 */
@RestController
@RequestMapping("/api")
public class ProcedimientoResource {

    private final Logger log = LoggerFactory.getLogger(ProcedimientoResource.class);

    private static final String ENTITY_NAME = "procedimiento";

    private final ProcedimientoRepository procedimientoRepository;

    public ProcedimientoResource(ProcedimientoRepository procedimientoRepository) {
        this.procedimientoRepository = procedimientoRepository;
    }

    /**
     * POST  /procedimientos : Create a new procedimiento.
     *
     * @param procedimiento the procedimiento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new procedimiento, or with status 400 (Bad Request) if the procedimiento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/procedimientos")
    @Timed
    public ResponseEntity<Procedimiento> createProcedimiento(@Valid @RequestBody Procedimiento procedimiento) throws URISyntaxException {
        log.debug("REST request to save Procedimiento : {}", procedimiento);
        if (procedimiento.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new procedimiento cannot already have an ID")).body(null);
        }
        Procedimiento result = procedimientoRepository.save(procedimiento);
        return ResponseEntity.created(new URI("/api/procedimientos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /procedimientos : Updates an existing procedimiento.
     *
     * @param procedimiento the procedimiento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated procedimiento,
     * or with status 400 (Bad Request) if the procedimiento is not valid,
     * or with status 500 (Internal Server Error) if the procedimiento couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/procedimientos")
    @Timed
    public ResponseEntity<Procedimiento> updateProcedimiento(@Valid @RequestBody Procedimiento procedimiento) throws URISyntaxException {
        log.debug("REST request to update Procedimiento : {}", procedimiento);
        if (procedimiento.getId() == null) {
            return createProcedimiento(procedimiento);
        }
        Procedimiento result = procedimientoRepository.save(procedimiento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, procedimiento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /procedimientos : get all the procedimientos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of procedimientos in body
     */
    @GetMapping("/procedimientos")
    @Timed
    public ResponseEntity<List<Procedimiento>> getAllProcedimientos(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Procedimientos");
        Page<Procedimiento> page = procedimientoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/procedimientos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /procedimientos/:id : get the "id" procedimiento.
     *
     * @param id the id of the procedimiento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the procedimiento, or with status 404 (Not Found)
     */
    @GetMapping("/procedimientos/{id}")
    @Timed
    public ResponseEntity<Procedimiento> getProcedimiento(@PathVariable Long id) {
        log.debug("REST request to get Procedimiento : {}", id);
        Procedimiento procedimiento = procedimientoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(procedimiento));
    }

    /**
     * DELETE  /procedimientos/:id : delete the "id" procedimiento.
     *
     * @param id the id of the procedimiento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/procedimientos/{id}")
    @Timed
    public ResponseEntity<Void> deleteProcedimiento(@PathVariable Long id) {
        log.debug("REST request to delete Procedimiento : {}", id);
        procedimientoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
