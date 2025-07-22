package com.sisol.sys_citas.model;

import jakarta.persistence.*;
import java.time.LocalTime; // Para horario de atención
// import com.sisol.sys_citas.enums.TurnoMedico; // Si quieres usarlo para el horario

@Entity
@Table(name = "medico") // Mapea a la tabla 'medico'
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID del médico, podría ser la misma PK que Usuario si es 1-1

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "cmp", unique = true, nullable = false, length = 20) // Colegiatura Médica del Perú
    private String cmp;

    @Column(name = "dni", unique = true, nullable = false, length = 8)
    private String dni;

    // Relación ManyToOne con Especialidad (un médico tiene una especialidad, una especialidad puede tener muchos médicos)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidad_id", nullable = false) // Columna FK en la tabla medico
    private Especialidad especialidad; // Necesitarás crear la entidad Especialidad.java

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "hora_inicio_atencion")
    private LocalTime horaInicioAtencion;

    @Column(name = "hora_fin_atencion")
    private LocalTime horaFinAtencion;

    // Relación OneToOne con Usuario (si cada médico es un usuario)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id") // La columna 'id' de medico es también la FK a usuario
    @MapsId // Indica que el id de Medico es también el id de Usuario
    private Usuario usuario;


    // --- Constructores ---
    public Medico() {}

    // Constructor con campos básicos
    public Medico(String nombres, String apellidos, String cmp, String dni, Especialidad especialidad, String telefono, String email, LocalTime horaInicioAtencion, LocalTime horaFinAtencion, Usuario usuario) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cmp = cmp;
        this.dni = dni;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.email = email;
        this.horaInicioAtencion = horaInicioAtencion;
        this.horaFinAtencion = horaFinAtencion;
        this.usuario = usuario;
    }

    // --- Getters y Setters ---
    // (Generar automáticamente en VS Code)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getCmp() { return cmp; }
    public void setCmp(String cmp) { this.cmp = cmp; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalTime getHoraInicioAtencion() { return horaInicioAtencion; }
    public void setHoraInicioAtencion(LocalTime horaInicioAtencion) { this.horaInicioAtencion = horaInicioAtencion; }
    public LocalTime getHoraFinAtencion() { return horaFinAtencion; }
    public void setHoraFinAtencion(LocalTime horaFinAtencion) { this.horaFinAtencion = horaFinAtencion; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}