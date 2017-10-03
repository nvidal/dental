package uy.dental.service.impl;

import uy.dental.service.SaldoService;
import uy.dental.domain.Saldo;
import uy.dental.repository.SaldoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Saldo.
 */
@Service
@Transactional
public class SaldoServiceImpl implements SaldoService{

    private final Logger log = LoggerFactory.getLogger(SaldoServiceImpl.class);

    private final SaldoRepository saldoRepository;

    public SaldoServiceImpl(SaldoRepository saldoRepository) {
        this.saldoRepository = saldoRepository;
    }

    /**
     * Save a saldo.
     *
     * @param saldo the entity to save
     * @return the persisted entity
     */
    @Override
    public Saldo save(Saldo saldo) {
        log.debug("Request to save Saldo : {}", saldo);
        return saldoRepository.save(saldo);
    }

    /**
     *  Get all the saldos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Saldo> findAll(Pageable pageable) {
        log.debug("Request to get all Saldos");
        return saldoRepository.findAll(pageable);
    }

    /**
     *  Get one saldo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Saldo findOne(Long id) {
        log.debug("Request to get Saldo : {}", id);
        return saldoRepository.findOne(id);
    }

    /**
     *  Delete the  saldo by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Saldo : {}", id);
        saldoRepository.delete(id);
    }
}
