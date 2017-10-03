package uy.dental.repository;

import uy.dental.domain.Procedimiento;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Procedimiento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcedimientoRepository extends JpaRepository<Procedimiento, Long> {

}
