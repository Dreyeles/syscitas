package com.sisol.sys_citas.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.sisol.sys_citas.enums.EstadoCivil;
import com.sisol.sys_citas.enums.Sexo;
import com.sisol.sys_citas.converter.EstadoCivilConverter;
import com.sisol.sys_citas.converter.SexoConverter;

@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "dni", unique = true, nullable = false, length = 8)
    private String dni;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Convert(converter = EstadoCivilConverter.class)
    @Column(name = "estado_civil", nullable = false)
    private EstadoCivil estadoCivil;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "distrito", length = 100)
    private String distrito;

    @Column(name = "contacto_emergencia_nombre", nullable = false, length = 100)
    private String contactoEmergenciaNombre;

    @Column(name = "contacto_emergencia_telefono", nullable = false, length = 11)
    private String contactoEmergenciaTelefono;

    @Column(name = "contacto_emergencia_parentesco", nullable = false, length = 20)
    private String contactoEmergenciaParentesco;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Convert(converter = SexoConverter.class)
    @Column(name = "sexo", nullable = false)
    private Sexo sexo;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    @MapsId
    private Usuario usuario;

    // --- Constructores ---
    public Paciente() {}

    public Paciente(String dni, String nombres, String apellidos, String telefono, 
                    EstadoCivil estadoCivil, String direccion, String distrito,
                    String contactoEmergenciaNombre, String contactoEmergenciaTelefono,
                    String contactoEmergenciaParentesco, LocalDate fechaNacimiento, 
                    Sexo sexo, Usuario usuario) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.estadoCivil = estadoCivil;
        this.direccion = direccion;
        this.distrito = distrito;
        this.contactoEmergenciaNombre = contactoEmergenciaNombre;
        this.contactoEmergenciaTelefono = contactoEmergenciaTelefono;
        this.contactoEmergenciaParentesco = contactoEmergenciaParentesco;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.usuario = usuario;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public EstadoCivil getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(EstadoCivil estadoCivil) { this.estadoCivil = estadoCivil; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }
    
    public String getContactoEmergenciaNombre() { return contactoEmergenciaNombre; }
    public void setContactoEmergenciaNombre(String contactoEmergenciaNombre) { this.contactoEmergenciaNombre = contactoEmergenciaNombre; }
    
    public String getContactoEmergenciaTelefono() { return contactoEmergenciaTelefono; }
    public void setContactoEmergenciaTelefono(String contactoEmergenciaTelefono) { this.contactoEmergenciaTelefono = contactoEmergenciaTelefono; }
    
    public String getContactoEmergenciaParentesco() { return contactoEmergenciaParentesco; }
    public void setContactoEmergenciaParentesco(String contactoEmergenciaParentesco) { this.contactoEmergenciaParentesco = contactoEmergenciaParentesco; }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}