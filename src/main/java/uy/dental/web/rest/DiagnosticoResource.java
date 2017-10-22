package uy.dental.web.rest;

import com.codahale.metrics.annotation.Timed;
import uy.dental.domain.Diagnostico;

import uy.dental.repository.DiagnosticoRepository;
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
 * REST controller for managing Diagnostico.
 */
@RestController
@RequestMapping("/api")
public class DiagnosticoResource {

    private final Logger log = LoggerFactory.getLogger(DiagnosticoResource.class);

    private static final String ENTITY_NAME = "diagnostico";

    private final DiagnosticoRepository diagnosticoRepository;

    public DiagnosticoResource(DiagnosticoRepository diagnosticoRepository) {
        this.diagnosticoRepository = diagnosticoRepository;
    }

    /**
     * POST  /diagnosticos : Create a new diagnostico.
     *
     * @param diagnostico the diagnostico to create
     * @return the ResponseEntity with status 201 (Created) and with body the new diagnostico, or with status 400 (Bad Request) if the diagnostico has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/diagnosticos")
    @Timed
    public ResponseEntity<Diagnostico> createDiagnostico(@Valid @RequestBody Diagnostico diagnostico) throws URISyntaxException {
        log.debug("REST request to save Diagnostico : {}", diagnostico);
        if (diagnostico.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new diagnostico cannot already have an ID")).body(null);
        }
        Diagnostico result = diagnosticoRepository.save(diagnostico);
        return ResponseEntity.created(new URI("/api/diagnosticos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /diagnosticos : Updates an existing diagnostico.
     *
     * @param diagnostico the diagnostico to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated diagnostico,
     * or with status 400 (Bad Request) if the diagnostico is not valid,
     * or with status 500 (Internal Server Error) if the diagnostico couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/diagnosticos")
    @Timed
    public ResponseEntity<Diagnostico> updateDiagnostico(@Valid @RequestBody Diagnostico diagnostico) throws URISyntaxException {
        log.debug("REST request to update Diagnostico : {}", diagnostico);
        if (diagnostico.getId() == null) {
            return createDiagnostico(diagnostico);
        }
        Diagnostico result = diagnosticoRepository.save(diagnostico);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, diagnostico.getId().toString()))
            .body(result);
    }

    /**
     * GET  /diagnosticos : get all the diagnosticos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of diagnosticos in body
     */
    @GetMapping("/diagnosticos")
    @Timed
    public ResponseEntity<List<Diagnostico>> getAllDiagnosticos(@ApiParam Pageable pageable, @RequestParam(value = "paciente",required = false) Long idPaciente) {
        log.debug("REST request to get a page of Diagnosticos");
        if (idPaciente!=null) {
            Page<Diagnostico> page = diagnosticoRepository.findByPacienteOrderByFechaDesc(idPaciente, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/diagnosticos");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }
        else{
            Page<Diagnostico> page = diagnosticoRepository.findAllByOrderByFechaDesc(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/diagnosticos");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }
    }

    /**
     * GET  /diagnosticos/:id : get the "id" diagnostico.
     *
     * @param id the id of the diagnostico to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the diagnostico, or with status 404 (Not Found)
     */
    @GetMapping("/diagnosticos/{id}")
    @Timed
    public ResponseEntity<Diagnostico> getDiagnostico(@PathVariable Long id) {
        log.debug("REST request to get Diagnostico : {}", id);
        Diagnostico diagnostico = diagnosticoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(diagnostico));
    }

    /**
     * DELETE  /diagnosticos/:id : delete the "id" diagnostico.
     *
     * @param id the id of the diagnostico to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/diagnosticos/{id}")
    @Timed
    public ResponseEntity<Void> deleteDiagnostico(@PathVariable Long id) {
        log.debug("REST request to delete Diagnostico : {}", id);
        diagnosticoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
