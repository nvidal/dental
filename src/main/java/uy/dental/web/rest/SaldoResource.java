package uy.dental.web.rest;

import com.codahale.metrics.annotation.Timed;
import uy.dental.domain.Saldo;
import uy.dental.service.SaldoService;
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
 * REST controller for managing Saldo.
 */
@RestController
@RequestMapping("/api")
public class SaldoResource {

    private final Logger log = LoggerFactory.getLogger(SaldoResource.class);

    private static final String ENTITY_NAME = "saldo";

    private final SaldoService saldoService;

    public SaldoResource(SaldoService saldoService) {
        this.saldoService = saldoService;
    }

    /**
     * POST  /saldos : Create a new saldo.
     *
     * @param saldo the saldo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saldo, or with status 400 (Bad Request) if the saldo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/saldos")
    @Timed
    public ResponseEntity<Saldo> createSaldo(@Valid @RequestBody Saldo saldo) throws URISyntaxException {
        log.debug("REST request to save Saldo : {}", saldo);
        if (saldo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new saldo cannot already have an ID")).body(null);
        }
        Saldo result = saldoService.save(saldo);
        return ResponseEntity.created(new URI("/api/saldos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /saldos : Updates an existing saldo.
     *
     * @param saldo the saldo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated saldo,
     * or with status 400 (Bad Request) if the saldo is not valid,
     * or with status 500 (Internal Server Error) if the saldo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/saldos")
    @Timed
    public ResponseEntity<Saldo> updateSaldo(@Valid @RequestBody Saldo saldo) throws URISyntaxException {
        log.debug("REST request to update Saldo : {}", saldo);
        if (saldo.getId() == null) {
            return createSaldo(saldo);
        }
        Saldo result = saldoService.save(saldo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, saldo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /saldos : get all the saldos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of saldos in body
     */
    @GetMapping("/saldos")
    @Timed
    public ResponseEntity<List<Saldo>> getAllSaldos(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Saldos");
        Page<Saldo> page = saldoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/saldos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /saldos/:id : get the "id" saldo.
     *
     * @param id the id of the saldo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saldo, or with status 404 (Not Found)
     */
    @GetMapping("/saldos/{id}")
    @Timed
    public ResponseEntity<Saldo> getSaldo(@PathVariable Long id) {
        log.debug("REST request to get Saldo : {}", id);
        Saldo saldo = saldoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(saldo));
    }

    /**
     * DELETE  /saldos/:id : delete the "id" saldo.
     *
     * @param id the id of the saldo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/saldos/{id}")
    @Timed
    public ResponseEntity<Void> deleteSaldo(@PathVariable Long id) {
        log.debug("REST request to delete Saldo : {}", id);
        saldoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
