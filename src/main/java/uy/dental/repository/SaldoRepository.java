package uy.dental.repository;

import uy.dental.domain.Saldo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Saldo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaldoRepository extends JpaRepository<Saldo, Long> {

}
