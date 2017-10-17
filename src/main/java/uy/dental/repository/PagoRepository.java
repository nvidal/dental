package uy.dental.repository;

import uy.dental.domain.Pago;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Pago entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

}
