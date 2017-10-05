package uy.dental.web.rest;

import com.codahale.metrics.annotation.Timed;
import uy.dental.domain.Cuenta;
import uy.dental.service.CuentaService;
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
 * REST controller for managing Cuenta.
 */
@RestController
@RequestMapping("/api")
public class CuentaResource {

    private final Logger log = LoggerFactory.getLogger(CuentaResource.class);

    private static final String ENTITY_NAME = "cuenta";

    private final CuentaService cuentaService;

    public CuentaResource(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    /**
     * POST  /cuentas : Create a new cuenta.
     *
     * @param cuenta the cuenta to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cuenta, or with status 400 (Bad Request) if the cuenta has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cuentas")
    @Timed
    public ResponseEntity<Cuenta> createCuenta(@Valid @RequestBody Cuenta cuenta) throws URISyntaxException {
        log.debug("REST request to save Cuenta : {}", cuenta);
        if (cuenta.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cuenta cannot already have an ID")).body(null);
        }
        Cuenta result = cuentaService.save(cuenta);
        return ResponseEntity.created(new URI("/api/cuentas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cuentas : Updates an existing cuenta.
     *
     * @param cuenta the cuenta to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cuenta,
     * or with status 400 (Bad Request) if the cuenta is not valid,
     * or with status 500 (Internal Server Error) if the cuenta couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cuentas")
    @Timed
    public ResponseEntity<Cuenta> updateCuenta(@Valid @RequestBody Cuenta cuenta) throws URISyntaxException {
        log.debug("REST request to update Cuenta : {}", cuenta);
        if (cuenta.getId() == null) {
            return createCuenta(cuenta);
        }
        Cuenta result = cuentaService.save(cuenta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cuenta.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cuentas : get all the cuentas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cuentas in body
     */
    @GetMapping("/cuentas")
    @Timed
    public ResponseEntity<List<Cuenta>> getAllCuentas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Cuentas");
        Page<Cuenta> page = cuentaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cuentas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cuentas/:id : get the "id" cuenta.
     *
     * @param id the id of the cuenta to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cuenta, or with status 404 (Not Found)
     */
    @GetMapping("/cuentas/{id}")
    @Timed
    public ResponseEntity<Cuenta> getCuenta(@PathVariable Long id) {
        log.debug("REST request to get Cuenta : {}", id);
        Cuenta cuenta = cuentaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cuenta));
    }

    /**
     * DELETE  /cuentas/:id : delete the "id" cuenta.
     *
     * @param id the id of the cuenta to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cuentas/{id}")
    @Timed
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long id) {
        log.debug("REST request to delete Cuenta : {}", id);
        cuentaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
