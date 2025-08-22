package com.sisol.sys_citas.dto;

import java.math.BigDecimal;

public class AgendarCitaDTO {
    private Long especialidadId;
    private Long servicioId;
    private Long medicoId;
    private Long disponibilidadId;
    private BigDecimal precio;

    public AgendarCitaDTO() {}

    public AgendarCitaDTO(Long especialidadId, Long servicioId, Long medicoId, Long disponibilidadId, BigDecimal precio) {
        this.especialidadId = especialidadId;
        this.servicioId = servicioId;
        this.medicoId = medicoId;
        this.disponibilidadId = disponibilidadId;
        this.precio = precio;
    }

    public Long getEspecialidadId() {
        return especialidadId;
    }

    public void setEspecialidadId(Long especialidadId) {
        this.especialidadId = especialidadId;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public Long getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Long medicoId) {
        this.medicoId = medicoId;
    }

    public Long getDisponibilidadId() {
        return disponibilidadId;
    }

    public void setDisponibilidadId(Long disponibilidadId) {
        this.disponibilidadId = disponibilidadId;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
} 