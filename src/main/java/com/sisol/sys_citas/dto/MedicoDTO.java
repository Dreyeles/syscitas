package com.sisol.sys_citas.dto;

public class MedicoDTO {
    private Long id;
    private String nombres;
    private String apellidos;
    private Long especialidadId;
    private String nombreCompleto;
    private String turno;

    public MedicoDTO() {}

    public MedicoDTO(Long id, String nombres, String apellidos, Long especialidadId) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.especialidadId = especialidadId;
        this.nombreCompleto = "Dr. " + getPrimerNombre(nombres) + " " + getPrimerApellido(apellidos);
    }

    public MedicoDTO(Long id, String nombres, String apellidos, Long especialidadId, String turno) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.especialidadId = especialidadId;
        this.turno = turno;
        this.nombreCompleto = "Dr. " + getPrimerNombre(nombres) + " " + getPrimerApellido(apellidos);
    }

    private String getPrimerNombre(String nombres) {
        if (nombres == null || nombres.trim().isEmpty()) {
            return "";
        }
        return nombres.trim().split("\\s+")[0];
    }

    private String getPrimerApellido(String apellidos) {
        if (apellidos == null || apellidos.trim().isEmpty()) {
            return "";
        }
        return apellidos.trim().split("\\s+")[0];
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Long getEspecialidadId() {
        return especialidadId;
    }

    public void setEspecialidadId(Long especialidadId) {
        this.especialidadId = especialidadId;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
} 