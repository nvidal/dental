package uy.dental.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Diagnostico.
 */
@Entity
@Table(name = "diagnostico")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Diagnostico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

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

    public Diagnostico fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Diagnostico descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Pieza getPieza() {
        return pieza;
    }

    public Diagnostico pieza(Pieza pieza) {
        this.pieza = pieza;
        return this;
    }

    public void setPieza(Pieza pieza) {
        this.pieza = pieza;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Diagnostico paciente(Paciente paciente) {
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
        Diagnostico diagnostico = (Diagnostico) o;
        if (diagnostico.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), diagnostico.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Diagnostico{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
