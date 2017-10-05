package uy.dental.repository;

import uy.dental.domain.Cuenta;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cuenta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

}
