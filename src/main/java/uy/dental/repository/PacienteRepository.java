package uy.dental.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uy.dental.domain.Paciente;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Paciente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    //OrApellidosContainsOrCedulaContains
    Page<Paciente> findByNombresContainsOrApellidosContainsOrCedulaContainsAllIgnoreCase(String nombre, String apellido, String cedula,
                                                      Pageable pageRequest);
}
