package com.sisol.sys_citas.model;

import jakarta.persistence.*;
// import java.util.Set; // Si quisieras una relación OneToMany con Citas

@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "costo", nullable = false)
    private Double costo;

    // Si un servicio puede tener muchas citas (relación inversa):
    // @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private Set<Cita> citas;

    // --- Constructores ---
    public Servicio() {}

    public Servicio(String nombre, String descripcion, Double costo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getCosto() { return costo; }
    public void setCosto(Double costo) { this.costo = costo; }
    // public Set<Cita> getCitas() { return citas; } // Si añades la relación OneToMany
    // public void setCitas(Set<Cita> citas) { this.citas = citas; }
}