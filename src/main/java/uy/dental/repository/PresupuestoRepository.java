package uy.dental.repository;

import uy.dental.domain.Presupuesto;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Presupuesto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {

}
