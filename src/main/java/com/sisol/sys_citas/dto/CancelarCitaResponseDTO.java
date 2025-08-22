package com.sisol.sys_citas.dto;

public class CancelarCitaResponseDTO {
    private String mensaje;
    private boolean exito;

    // Constructores
    public CancelarCitaResponseDTO() {}

    public CancelarCitaResponseDTO(String mensaje, boolean exito) {
        this.mensaje = mensaje;
        this.exito = exito;
    }

    // Getters y Setters
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }
}
