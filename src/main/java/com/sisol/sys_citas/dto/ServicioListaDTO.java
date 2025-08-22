package com.sisol.sys_citas.dto;

import java.math.BigDecimal;

public class ServicioListaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioServicio;
    private EspecialidadDTO especialidad;

    public ServicioListaDTO() {}

    public ServicioListaDTO(Long id, String nombre, String descripcion, BigDecimal precioServicio, 
                           EspecialidadDTO especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioServicio = precioServicio;
        this.especialidad = especialidad;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecioServicio() { return precioServicio; }
    public void setPrecioServicio(BigDecimal precioServicio) { this.precioServicio = precioServicio; }

    public EspecialidadDTO getEspecialidad() { return especialidad; }
    public void setEspecialidad(EspecialidadDTO especialidad) { this.especialidad = especialidad; }
}
