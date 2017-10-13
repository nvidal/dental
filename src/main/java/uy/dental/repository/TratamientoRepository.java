package uy.dental.repository;

import uy.dental.domain.Tratamiento;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tratamiento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TratamientoRepository extends JpaRepository<Tratamiento, Long> {

}
