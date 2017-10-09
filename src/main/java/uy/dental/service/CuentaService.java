package uy.dental.service;

import uy.dental.domain.Cuenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Cuenta.
 */
public interface CuentaService {

    /**
     * Save a cuenta.
     *
     * @param cuenta the entity to save
     * @return the persisted entity
     */
    Cuenta save(Cuenta cuenta);

    /**
     *  Get all the cuentas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Cuenta> findAll(Pageable pageable);

    /**
     *  Get the "id" cuenta.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Cuenta findOne(Long id);

    /**
     *  Delete the "id" cuenta.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);


    /**
     *  Devuelve el saldo del paciente "idPaciente".
     *  Saldo = SUM(haber) - SUM(debe)
     *
     *  @param idPaciente the id of the Paciente
     */
    Float getSaldoPaciente(Long idPaciente);
}
