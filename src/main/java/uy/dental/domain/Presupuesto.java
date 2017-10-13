package uy.dental.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Presupuesto.
 */
@Entity
@Table(name = "presupuesto")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Presupuesto implements Serializable {

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

    @NotNull
    @Column(name = "precio", nullable = false)
    private Float precio;

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

    public Presupuesto fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Presupuesto descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getPrecio() {
        return precio;
    }

    public Presupuesto precio(Float precio) {
        this.precio = precio;
        return this;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Presupuesto paciente(Paciente paciente) {
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
        Presupuesto presupuesto = (Presupuesto) o;
        if (presupuesto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), presupuesto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Presupuesto{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", precio='" + getPrecio() + "'" +
            "}";
    }
}
