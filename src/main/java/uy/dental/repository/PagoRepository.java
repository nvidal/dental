package uy.dental.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import uy.dental.domain.Pago;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Pago entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    Page<Pago> findAllByOrderByFechaDesc(Pageable pageable);

    @Query("SELECT p FROM Pago p WHERE p.paciente.id = :idPaciente")
    Page<Pago> findByPacienteOrderByFechaDesc(@Param("idPaciente")Long idPaciente, Pageable pageable);
}
