package uy.dental.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import uy.dental.domain.Nota;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Nota entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {

    Page<Nota> findAllByOrderByFechaDesc(Pageable pageable);

    @Query("SELECT n FROM Nota n WHERE n.paciente.id = :idPaciente")
    Page<Nota> findByPacienteOrderByFechaDesc(@Param("idPaciente")Long idPaciente, Pageable pageable);
}
