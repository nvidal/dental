package uy.dental.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Paciente.
 */
@Entity
@Table(name = "paciente")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "fecha")
    private LocalDate fecha;

    @NotNull
    @Column(name = "nombres", nullable = false)
    private String nombres;

    @NotNull
    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @NotNull
    @Column(name = "cedula", nullable = false)
    private String cedula;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "celular")
    private String celular;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "mail")
    private String mail;

    @Column(name = "alergico")
    private String alergico;

    @Column(name = "diabetes")
    private String diabetes;

    @Column(name = "presion_alta")
    private String presionAlta;

    @Column(name = "tiroides")
    private String tiroides;

    @Column(name = "cicatrizacion")
    private String cicatrizacion;

    @Column(name = "cardiaca")
    private String cardiaca;

    @Column(name = "farmacos")
    private String farmacos;

    @OneToMany(mappedBy = "paciente")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tratamiento> tratamientos = new HashSet<>();

    @OneToMany(mappedBy = "paciente")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Pago> pagos = new HashSet<>();

    @OneToMany(mappedBy = "paciente")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Diagnostico> diagnosticos = new HashSet<>();

    @OneToMany(mappedBy = "paciente")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Nota> notas = new HashSet<>();

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

    public Paciente fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getNombres() {
        return nombres;
    }

    public Paciente nombres(String nombres) {
        this.nombres = nombres;
        return this;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public Paciente apellidos(String apellidos) {
        this.apellidos = apellidos;
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCedula() {
        return cedula;
    }

    public Paciente cedula(String cedula) {
        this.cedula = cedula;
        return this;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public Paciente telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public Paciente celular(String celular) {
        this.celular = celular;
        return this;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public Paciente direccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMail() {
        return mail;
    }

    public Paciente mail(String mail) {
        this.mail = mail;
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAlergico() {
        return alergico;
    }

    public Paciente alergico(String alergico) {
        this.alergico = alergico;
        return this;
    }

    public void setAlergico(String alergico) {
        this.alergico = alergico;
    }

    public String getDiabetes() {
        return diabetes;
    }

    public Paciente diabetes(String diabetes) {
        this.diabetes = diabetes;
        return this;
    }

    public void setDiabetes(String diabetes) {
        this.diabetes = diabetes;
    }

    public String getPresionAlta() {
        return presionAlta;
    }

    public Paciente presionAlta(String presionAlta) {
        this.presionAlta = presionAlta;
        return this;
    }

    public void setPresionAlta(String presionAlta) {
        this.presionAlta = presionAlta;
    }

    public String getTiroides() {
        return tiroides;
    }

    public Paciente tiroides(String tiroides) {
        this.tiroides = tiroides;
        return this;
    }

    public void setTiroides(String tiroides) {
        this.tiroides = tiroides;
    }

    public String getCicatrizacion() {
        return cicatrizacion;
    }

    public Paciente cicatrizacion(String cicatrizacion) {
        this.cicatrizacion = cicatrizacion;
        return this;
    }

    public void setCicatrizacion(String cicatrizacion) {
        this.cicatrizacion = cicatrizacion;
    }

    public String getCardiaca() {
        return cardiaca;
    }

    public Paciente cardiaca(String cardiaca) {
        this.cardiaca = cardiaca;
        return this;
    }

    public void setCardiaca(String cardiaca) {
        this.cardiaca = cardiaca;
    }

    public String getFarmacos() {
        return farmacos;
    }

    public Paciente farmacos(String farmacos) {
        this.farmacos = farmacos;
        return this;
    }

    public void setFarmacos(String farmacos) {
        this.farmacos = farmacos;
    }

    public Set<Tratamiento> getTratamientos() {
        return tratamientos;
    }

    public Paciente tratamientos(Set<Tratamiento> tratamientos) {
        this.tratamientos = tratamientos;
        return this;
    }

    public Paciente addTratamientos(Tratamiento tratamiento) {
        this.tratamientos.add(tratamiento);
        tratamiento.setPaciente(this);
        return this;
    }

    public Paciente removeTratamientos(Tratamiento tratamiento) {
        this.tratamientos.remove(tratamiento);
        tratamiento.setPaciente(null);
        return this;
    }

    public void setTratamientos(Set<Tratamiento> tratamientos) {
        this.tratamientos = tratamientos;
    }

    public Set<Pago> getPagos() {
        return pagos;
    }

    public Paciente pagos(Set<Pago> pagos) {
        this.pagos = pagos;
        return this;
    }

    public Paciente addPagos(Pago pago) {
        this.pagos.add(pago);
        pago.setPaciente(this);
        return this;
    }

    public Paciente removePagos(Pago pago) {
        this.pagos.remove(pago);
        pago.setPaciente(null);
        return this;
    }

    public void setPagos(Set<Pago> pagos) {
        this.pagos = pagos;
    }

    public Set<Diagnostico> getDiagnosticos() {
        return diagnosticos;
    }

    public Paciente diagnosticos(Set<Diagnostico> diagnosticos) {
        this.diagnosticos = diagnosticos;
        return this;
    }

    public Paciente addDiagnosticos(Diagnostico diagnostico) {
        this.diagnosticos.add(diagnostico);
        diagnostico.setPaciente(this);
        return this;
    }

    public Paciente removeDiagnosticos(Diagnostico diagnostico) {
        this.diagnosticos.remove(diagnostico);
        diagnostico.setPaciente(null);
        return this;
    }

    public void setDiagnosticos(Set<Diagnostico> diagnosticos) {
        this.diagnosticos = diagnosticos;
    }

    public Set<Nota> getNotas() {
        return notas;
    }

    public Paciente notas(Set<Nota> notas) {
        this.notas = notas;
        return this;
    }

    public Paciente addNotas(Nota nota) {
        this.notas.add(nota);
        nota.setPaciente(this);
        return this;
    }

    public Paciente removeNotas(Nota nota) {
        this.notas.remove(nota);
        nota.setPaciente(null);
        return this;
    }

    public void setNotas(Set<Nota> notas) {
        this.notas = notas;
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
        Paciente paciente = (Paciente) o;
        if (paciente.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paciente.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Paciente{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", nombres='" + getNombres() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", cedula='" + getCedula() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", celular='" + getCelular() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", mail='" + getMail() + "'" +
            ", alergico='" + getAlergico() + "'" +
            ", diabetes='" + getDiabetes() + "'" +
            ", presionAlta='" + getPresionAlta() + "'" +
            ", tiroides='" + getTiroides() + "'" +
            ", cicatrizacion='" + getCicatrizacion() + "'" +
            ", cardiaca='" + getCardiaca() + "'" +
            ", farmacos='" + getFarmacos() + "'" +
            "}";
    }

    @Formula("(select " +
        "(select  coalesce(sum(coalesce(p.monto, 0)),0) from Pago p where  p.paciente_id = id) - " +
        "(select coalesce(sum(coalesce(t.precio, 0)),0) from Tratamiento t where t.paciente_id = id) )")
    private Float saldo;

    public Float getSaldo() {
        return saldo;
    }

    public void setSaldo(Float saldo) {
        this.saldo = saldo;
    }

    @Formula("(select max(t.fecha) from Tratamiento t where t.paciente_id = id)")
    private LocalDate ultimaVisita;

    public LocalDate getUltimaVisita() {
        return ultimaVisita;
    }

    public void setUltimaVisita(LocalDate ultimaVisita) {
        this.ultimaVisita = ultimaVisita;
    }
}
