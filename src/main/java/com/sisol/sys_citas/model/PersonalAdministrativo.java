package com.sisol.sys_citas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "personal_administrativo")
public class PersonalAdministrativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID del personal administrativo

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "dni", unique = true, nullable = false, length = 8)
    private String dni;

    @Column(name = "cargo", length = 100)
    private String cargo; // Ej: "Recepcionista", "Administrador"

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    // Relación OneToOne con Usuario (si cada personal administrativo es un usuario)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id") // La columna 'id' de personal_administrativo es también la FK a usuario
    @MapsId // Indica que el id de PersonalAdministrativo es también el id de Usuario
    private Usuario usuario; // Asegúrate de tener la entidad Usuario

    // --- Constructores ---
    public PersonalAdministrativo() {}

    public PersonalAdministrativo(String nombres, String apellidos, String dni, String cargo, String telefono, String email, Usuario usuario) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.cargo = cargo;
        this.telefono = telefono;
        this.email = email;
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
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
