package com.sisol.sys_citas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.sisol.sys_citas.enums.Rol;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 20) // Agregado length para el enum
    private Rol rol;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "activo", nullable = false)
    private Integer activo;

    @Column(name = "correo", unique = true, nullable = false, length = 100) // Agregado length
    private String correo;

    @Column(name = "contrasenia", nullable = false, length = 60) // Agregado length para BCrypt
    private String contrasenia;

    // --- ¡Añade esta relación OneToOne! ---
    // 'mappedBy' indica que el campo 'usuario' en la entidad Paciente es el dueño de la relación.
    // CascadeType.ALL: Si se guarda/elimina Usuario, se propaga a Paciente.
    // orphanRemoval = true: Si se desvincula un Paciente de Usuario, se elimina ese Paciente.
    // fetch = FetchType.LAZY: Carga el Paciente solo cuando es necesario.
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Paciente paciente; // Puede ser null si el usuario no es paciente (ej. médico, admin)

    // --- Constructores ---
    public Usuario() {
    }

    // Constructor existente (puedes añadir el paciente aquí si lo usas en un constructor completo)
    public Usuario(Rol rol, LocalDateTime fechaRegistro, LocalDateTime fechaActualizacion, Integer activo, String correo, String contrasenia) {
        this.rol = rol;
        this.fechaRegistro = fechaRegistro;
        this.fechaActualizacion = fechaActualizacion;
        this.activo = activo;
        this.correo = correo;
        this.contrasenia = contrasenia;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public Integer getActivo() { return activo; }
    public void setActivo(Integer activo) { this.activo = activo; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    // Getter y Setter para la relación Paciente
    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        this.activo = 1;
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}