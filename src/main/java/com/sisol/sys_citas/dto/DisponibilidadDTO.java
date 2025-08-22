package com.sisol.sys_citas.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class DisponibilidadDTO {
    private Long id;
    private Long medicoId;
    private LocalDate fechaDisponibilidad;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Boolean disponible;

    public DisponibilidadDTO() {}

    public DisponibilidadDTO(Long id, Long medicoId, LocalDate fechaDisponibilidad, LocalTime horaInicio, LocalTime horaFin, Boolean disponible) {
        this.id = id;
        this.medicoId = medicoId;
        this.fechaDisponibilidad = fechaDisponibilidad;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.disponible = disponible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Long medicoId) {
        this.medicoId = medicoId;
    }

    public LocalDate getFechaDisponibilidad() {
        return fechaDisponibilidad;
    }

    public void setFechaDisponibilidad(LocalDate fechaDisponibilidad) {
        this.fechaDisponibilidad = fechaDisponibilidad;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
} 