package com.sisol.sys_citas.model;

import jakarta.persistence.*;
// import java.util.Set; // Para la relación OneToMany (opcional, si un Especialidad tiene muchos Medicos)

@Entity
@Table(name = "especialidad") // Mapea a la tabla 'especialidad'
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    // Si quieres que Especialidad sepa qué médicos tiene, podrías añadir esto:
    // @OneToMany(mappedBy = "especialidad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private Set<Medico> medicos;


    // --- Constructores ---
    public Especialidad() {}

    public Especialidad(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    // public Set<Medico> getMedicos() { return medicos; } // Si añades la relación OneToMany
    // public void setMedicos(Set<Medico> medicos) { this.medicos = medicos; }
}