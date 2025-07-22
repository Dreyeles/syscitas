package com.sisol.sys_citas.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.sisol.sys_citas.enums.EstadoCivil;
import com.sisol.sys_citas.enums.GrupoSanguineo;
import com.sisol.sys_citas.enums.Sexo;

@Entity
@Table(name = "paciente") // Mapea a la tabla 'paciente'
public class Paciente {

    @Id // Marca este campo como la clave primaria
    @Column(name = "id") // Se añade explícitamente el nombre de la columna para claridad
    private Long id; // El ID de Paciente será el mismo que el de Usuario

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "dni", unique = true, nullable = false, length = 8)
    private String dni;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", nullable = false)
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil", nullable = false)
    private EstadoCivil estadoCivil;

    @Column(name = "direccion", nullable = false, length = 255)
    private String direccion;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Column(name = "alergias", length = 500) // Este campo puede ser nulo, según tu DDL (DEFAULT NULL)
    private String alergias;

    @Enumerated(EnumType.STRING)
    @Column(name = "grupo_sanguineo", nullable = false)
    private GrupoSanguineo grupoSanguineo;

    // --- NUEVOS CAMPOS AÑADIDOS para `paciente` (obligatorios en tu DB) ---
    @Column(name = "contacto_emergencia_nombre", nullable = false, length = 100)
    private String contactoEmergenciaNombre;

    @Column(name = "contacto_emergencia_telefono", nullable = false, length = 11) // Revisa la longitud si es más larga
    private String contactoEmergenciaTelefono;
    // --- FIN NUEVOS CAMPOS ---

    // Relación OneToOne con Usuario (si cada paciente es un usuario)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id") // La columna 'id' de paciente es también la FK a usuario
    @MapsId // Indica que el id de Paciente es también el id de Usuario
    private Usuario usuario;

    // --- Constructores ---
    public Paciente() {}

    // Constructor con campos básicos (actualizado para incluir los nuevos campos)
    public Paciente(String nombres, String apellidos, String dni, LocalDate fechaNacimiento, Sexo sexo,
                    EstadoCivil estadoCivil, String direccion, String telefono, String alergias,
                    GrupoSanguineo grupoSanguineo, String contactoEmergenciaNombre,
                    String contactoEmergenciaTelefono, Usuario usuario) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.estadoCivil = estadoCivil;
        this.direccion = direccion;
        this.telefono = telefono;
        this.alergias = alergias;
        this.grupoSanguineo = grupoSanguineo;
        this.contactoEmergenciaNombre = contactoEmergenciaNombre;
        this.contactoEmergenciaTelefono = contactoEmergenciaTelefono;
        this.usuario = usuario;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }
    public EstadoCivil getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(EstadoCivil estadoCivil) { this.estadoCivil = estadoCivil; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }
    public GrupoSanguineo getGrupoSanguineo() { return grupoSanguineo; }
    public void setGrupoSanguineo(GrupoSanguineo grupoSanguineo) { this.grupoSanguineo = grupoSanguineo; }

    // --- Getters y Setters para los nuevos campos ---
    public String getContactoEmergenciaNombre() { return contactoEmergenciaNombre; }
    public void setContactoEmergenciaNombre(String contactoEmergenciaNombre) { this.contactoEmergenciaNombre = contactoEmergenciaNombre; }
    public String getContactoEmergenciaTelefono() { return contactoEmergenciaTelefono; }
    public void setContactoEmergenciaTelefono(String contactoEmergenciaTelefono) { this.contactoEmergenciaTelefono = contactoEmergenciaTelefono; }
    // --- FIN Getters y Setters para los nuevos campos ---

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}