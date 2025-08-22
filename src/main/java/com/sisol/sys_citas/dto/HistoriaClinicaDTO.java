package com.sisol.sys_citas.dto;

import com.sisol.sys_citas.enums.GrupoSanguineo;
import java.time.LocalDateTime;

public class HistoriaClinicaDTO {
    
    private Long id;
    private Long pacienteId;
    private GrupoSanguineo grupoSanguineo;
    private String alergias;
    private String enfermedadesCronicas;
    private String antecedentesPersonales;
    private String antecedentesFamiliares;
    private String medicacionActual;
    private String discapacidad;
    private String vacunacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Campo adicional para mostrar información del paciente
    private String nombrePaciente;

    // Constructores
    public HistoriaClinicaDTO() {}

    public HistoriaClinicaDTO(Long id, Long pacienteId, GrupoSanguineo grupoSanguineo, 
                             String alergias, String enfermedadesCronicas, 
                             String antecedentesPersonales, String antecedentesFamiliares,
                             String medicacionActual, String discapacidad, String vacunacion,
                             LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.grupoSanguineo = grupoSanguineo;
        this.alergias = alergias;
        this.enfermedadesCronicas = enfermedadesCronicas;
        this.antecedentesPersonales = antecedentesPersonales;
        this.antecedentesFamiliares = antecedentesFamiliares;
        this.medicacionActual = medicacionActual;
        this.discapacidad = discapacidad;
        this.vacunacion = vacunacion;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public GrupoSanguineo getGrupoSanguineo() { return grupoSanguineo; }
    public void setGrupoSanguineo(GrupoSanguineo grupoSanguineo) { this.grupoSanguineo = grupoSanguineo; }

    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public String getEnfermedadesCronicas() { return enfermedadesCronicas; }
    public void setEnfermedadesCronicas(String enfermedadesCronicas) { this.enfermedadesCronicas = enfermedadesCronicas; }

    public String getAntecedentesPersonales() { return antecedentesPersonales; }
    public void setAntecedentesPersonales(String antecedentesPersonales) { this.antecedentesPersonales = antecedentesPersonales; }

    public String getAntecedentesFamiliares() { return antecedentesFamiliares; }
    public void setAntecedentesFamiliares(String antecedentesFamiliares) { this.antecedentesFamiliares = antecedentesFamiliares; }

    public String getMedicacionActual() { return medicacionActual; }
    public void setMedicacionActual(String medicacionActual) { this.medicacionActual = medicacionActual; }

    public String getDiscapacidad() { return discapacidad; }
    public void setDiscapacidad(String discapacidad) { this.discapacidad = discapacidad; }

    public String getVacunacion() { return vacunacion; }
    public void setVacunacion(String vacunacion) { this.vacunacion = vacunacion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }
} 