package uy.dental.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Pieza.
 */
@Entity
@Table(name = "pieza")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Pieza implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "numero_pieza", nullable = false)
    private Integer numeroPieza;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroPieza() {
        return numeroPieza;
    }

    public Pieza numeroPieza(Integer numeroPieza) {
        this.numeroPieza = numeroPieza;
        return this;
    }

    public void setNumeroPieza(Integer numeroPieza) {
        this.numeroPieza = numeroPieza;
    }

    public String getNombre() {
        return nombre;
    }

    public Pieza nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        Pieza pieza = (Pieza) o;
        if (pieza.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pieza.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pieza{" +
            "id=" + getId() +
            ", numeroPieza='" + getNumeroPieza() + "'" +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
