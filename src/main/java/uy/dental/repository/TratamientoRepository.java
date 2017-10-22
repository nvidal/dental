package uy.dental.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import uy.dental.domain.Tratamiento;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tratamiento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TratamientoRepository extends JpaRepository<Tratamiento, Long> {


    Page<Tratamiento> findAllByOrderByFechaDesc(Pageable pageable);

    @Query("SELECT t FROM Tratamiento t WHERE t.paciente.id = :idPaciente")
    Page<Tratamiento> findByPacienteOrderByFechaDesc(@Param("idPaciente")Long idPaciente, Pageable pageable);
}
