package uy.dental.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import uy.dental.domain.Diagnostico;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Diagnostico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

    Page<Diagnostico> findAllByOrderByFechaDesc(Pageable pageable);

    @Query("SELECT d FROM Diagnostico d WHERE d.paciente.id = :idPaciente")
    Page<Diagnostico> findByPacienteOrderByFechaDesc(@Param("idPaciente")Long idPaciente, Pageable pageable);
}
