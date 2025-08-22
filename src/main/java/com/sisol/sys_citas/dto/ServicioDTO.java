package com.sisol.sys_citas.dto;

import java.math.BigDecimal;

public class ServicioDTO {
    private Long id;
    private String nombre;
    private BigDecimal precioServicio;
    private Long especialidadId;

    public ServicioDTO() {}

    public ServicioDTO(Long id, String nombre, BigDecimal precioServicio, Long especialidadId) {
        this.id = id;
        this.nombre = nombre;
        this.precioServicio = precioServicio;
        this.especialidadId = especialidadId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecioServicio() {
        return precioServicio;
    }

    public void setPrecioServicio(BigDecimal precioServicio) {
        this.precioServicio = precioServicio;
    }

    public Long getEspecialidadId() {
        return especialidadId;
    }

    public void setEspecialidadId(Long especialidadId) {
        this.especialidadId = especialidadId;
    }
} 