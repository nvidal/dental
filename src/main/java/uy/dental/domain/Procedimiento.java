package uy.dental.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Procedimiento.
 */
@Entity
@Table(name = "procedimiento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Procedimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "procedimiento", nullable = false)
    private String procedimiento;

    @ManyToOne
    private Pieza pieza;

    @ManyToOne(optional = false)
    @NotNull
    private Paciente paciente;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Procedimiento fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public Procedimiento procedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
        return this;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public Pieza getPieza() {
        return pieza;
    }

    public Procedimiento pieza(Pieza pieza) {
        this.pieza = pieza;
        return this;
    }

    public void setPieza(Pieza pieza) {
        this.pieza = pieza;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Procedimiento paciente(Paciente paciente) {
        this.paciente = paciente;
        return this;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Procedimiento procedimiento = (Procedimiento) o;
        if (procedimiento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), procedimiento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Procedimiento{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", procedimiento='" + getProcedimiento() + "'" +
            "}";
    }
}
