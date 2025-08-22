package com.sisol.sys_citas.dto;

import com.sisol.sys_citas.enums.EstadoAtencion;
import java.time.LocalDateTime;

public class AtencionMedicaDTO {
    
    private Long id;
    private Long citaId;
    private Long medicoId;
    private String motivoConsulta;
    private String diagnostico;
    private String tratamiento;
    private String recomendaciones;
    private LocalDateTime fechaAtencion;
    private EstadoAtencion estado;
    
    // Campos adicionales para mostrar información relacionada
    private String nombrePaciente;
    private String nombreMedico;
    private String especialidad;

    // Constructores
    public AtencionMedicaDTO() {}

    public AtencionMedicaDTO(Long id, Long citaId, Long medicoId, String motivoConsulta, 
                            String diagnostico, String tratamiento, String recomendaciones, 
                            LocalDateTime fechaAtencion, EstadoAtencion estado) {
        this.id = id;
        this.citaId = citaId;
        this.medicoId = medicoId;
        this.motivoConsulta = motivoConsulta;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.recomendaciones = recomendaciones;
        this.fechaAtencion = fechaAtencion;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }

    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }

    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public String getRecomendaciones() { return recomendaciones; }
    public void setRecomendaciones(String recomendaciones) { this.recomendaciones = recomendaciones; }

    public LocalDateTime getFechaAtencion() { return fechaAtencion; }
    public void setFechaAtencion(LocalDateTime fechaAtencion) { this.fechaAtencion = fechaAtencion; }

    public EstadoAtencion getEstado() { return estado; }
    public void setEstado(EstadoAtencion estado) { this.estado = estado; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public String getNombreMedico() { return nombreMedico; }
    public void setNombreMedico(String nombreMedico) { this.nombreMedico = nombreMedico; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
} 