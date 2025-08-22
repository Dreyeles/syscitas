package com.sisol.sys_citas.dto;

public class CancelarCitaDTO {
    private Integer numTicket;
    private String motivoCancelacion;

    // Constructores
    public CancelarCitaDTO() {}

    public CancelarCitaDTO(Integer numTicket, String motivoCancelacion) {
        this.numTicket = numTicket;
        this.motivoCancelacion = motivoCancelacion;
    }

    // Getters y Setters
    public Integer getNumTicket() {
        return numTicket;
    }

    public void setNumTicket(Integer numTicket) {
        this.numTicket = numTicket;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public void setMotivoCancelacion(String motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }
}
