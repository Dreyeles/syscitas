package com.sisol.sys_citas.dto;

public class ReagendarCitaResponseDTO {
    private String mensaje;
    private boolean exito;
    private String nuevaFecha;
    private String nuevaHora;

    // Constructor vacío
    public ReagendarCitaResponseDTO() {}

    // Constructor con parámetros
    public ReagendarCitaResponseDTO(String mensaje, boolean exito) {
        this.mensaje = mensaje;
        this.exito = exito;
    }

    // Constructor completo
    public ReagendarCitaResponseDTO(String mensaje, boolean exito, String nuevaFecha, String nuevaHora) {
        this.mensaje = mensaje;
        this.exito = exito;
        this.nuevaFecha = nuevaFecha;
        this.nuevaHora = nuevaHora;
    }

    // Getters y setters
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

    public String getNuevaFecha() {
        return nuevaFecha;
    }

    public void setNuevaFecha(String nuevaFecha) {
        this.nuevaFecha = nuevaFecha;
    }

    public String getNuevaHora() {
        return nuevaHora;
    }

    public void setNuevaHora(String nuevaHora) {
        this.nuevaHora = nuevaHora;
    }
}
