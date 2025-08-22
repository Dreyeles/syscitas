package com.sisol.sys_citas.dto;

public class EspecialidadDTO {
    private Long id;
    private String nombre;

    public EspecialidadDTO() {}

    public EspecialidadDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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
} 