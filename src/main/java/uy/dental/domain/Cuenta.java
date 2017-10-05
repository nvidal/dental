package uy.dental.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Cuenta.
 */
@Entity
@Table(name = "cuenta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cuenta implements Serializable {

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

    @Column(name = "debe")
    private Float debe;

    @Column(name = "haber")
    private Float haber;

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

    public Cuenta fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Cuenta descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getDebe() {
        return debe;
    }

    public Cuenta debe(Float debe) {
        this.debe = debe;
        return this;
    }

    public void setDebe(Float debe) {
        this.debe = debe;
    }

    public Float getHaber() {
        return haber;
    }

    public Cuenta haber(Float haber) {
        this.haber = haber;
        return this;
    }

    public void setHaber(Float haber) {
        this.haber = haber;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Cuenta paciente(Paciente paciente) {
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
        Cuenta cuenta = (Cuenta) o;
        if (cuenta.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cuenta.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cuenta{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", debe='" + getDebe() + "'" +
            ", haber='" + getHaber() + "'" +
            "}";
    }
}
