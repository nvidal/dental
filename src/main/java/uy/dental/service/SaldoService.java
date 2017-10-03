package uy.dental.service;

import uy.dental.domain.Saldo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Saldo.
 */
public interface SaldoService {

    /**
     * Save a saldo.
     *
     * @param saldo the entity to save
     * @return the persisted entity
     */
    Saldo save(Saldo saldo);

    /**
     *  Get all the saldos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Saldo> findAll(Pageable pageable);

    /**
     *  Get the "id" saldo.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Saldo findOne(Long id);

    /**
     *  Delete the "id" saldo.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
