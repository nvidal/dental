package uy.dental.repository;

import uy.dental.domain.Diagnostico;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Diagnostico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

}
