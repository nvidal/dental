package uy.dental.repository;

import uy.dental.domain.Pieza;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Pieza entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PiezaRepository extends JpaRepository<Pieza, Long> {

}
