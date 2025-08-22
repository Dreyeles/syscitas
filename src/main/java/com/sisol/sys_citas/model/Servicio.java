package com.sisol.sys_citas.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
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

    @Column(name = "precio_servicio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioServicio;

    // Relación ManyToOne con Especialidad
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidad_id", nullable = false)
    private Especialidad especialidad;

    // Si un servicio puede tener muchas citas (relación inversa):
    // @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private Set<Cita> citas;

    // --- Constructores ---
    public Servicio() {}

    public Servicio(String nombre, String descripcion, BigDecimal precioServicio, Especialidad especialidad) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioServicio = precioServicio;
        this.especialidad = especialidad;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecioServicio() { return precioServicio; }
    public void setPrecioServicio(BigDecimal precioServicio) { this.precioServicio = precioServicio; }
    
    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }
    
    // public Set<Cita> getCitas() { return citas; } // Si añades la relación OneToMany
    // public void setCitas(Set<Cita> citas) { this.citas = citas; }
}