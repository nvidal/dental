package uy.dental.service.impl;

import uy.dental.domain.Paciente;
import uy.dental.service.CuentaService;
import uy.dental.domain.Cuenta;
import uy.dental.repository.CuentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing Cuenta.
 */
@Service
@Transactional
public class CuentaServiceImpl implements CuentaService{

    private final Logger log = LoggerFactory.getLogger(CuentaServiceImpl.class);

    private final CuentaRepository cuentaRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    /**
     * Save a cuenta.
     *
     * @param cuenta the entity to save
     * @return the persisted entity
     */
    @Override
    public Cuenta save(Cuenta cuenta) {
        log.debug("Request to save Cuenta : {}", cuenta);
        return cuentaRepository.save(cuenta);
    }

    /**
     *  Get all the cuentas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Cuenta> findAll(Pageable pageable) {
        log.debug("Request to get all Cuentas");
        return cuentaRepository.findAll(pageable);
    }

    /**
     *  Get one cuenta by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Cuenta findOne(Long id) {
        log.debug("Request to get Cuenta : {}", id);
        return cuentaRepository.findOne(id);
    }

    /**
     *  Delete the  cuenta by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cuenta : {}", id);
        cuentaRepository.delete(id);
    }


    /**
     *  Devuelve el saldo del paciente "idPaciente".
     *  Saldo = SUM(haber) - SUM(debe)
     *
     *  @param idPaciente the id of the Paciente
     */
    @Override
    @Transactional(readOnly = true)
    public Float getSaldoPaciente(Long idPaciente) {
        log.debug("Request to get Cuentas byPaciente "+idPaciente);
        List<Cuenta> cuentas = cuentaRepository.findByPaciente_Id(idPaciente);
        Float saldo = 0F;
        for(Cuenta cuenta : cuentas){
            saldo += cuenta.getHaber() - cuenta.getDebe();
        }
        return saldo;
    }
}
