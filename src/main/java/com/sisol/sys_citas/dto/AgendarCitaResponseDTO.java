package com.sisol.sys_citas.dto;

public class AgendarCitaResponseDTO {
    private String mensaje;
    private String nombrePaciente;
    private String apellidosPaciente;

    public AgendarCitaResponseDTO() {}

    public AgendarCitaResponseDTO(String mensaje, String nombrePaciente, String apellidosPaciente) {
        this.mensaje = mensaje;
        this.nombrePaciente = nombrePaciente;
        this.apellidosPaciente = apellidosPaciente;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getApellidosPaciente() {
        return apellidosPaciente;
    }

    public void setApellidosPaciente(String apellidosPaciente) {
        this.apellidosPaciente = apellidosPaciente;
    }
}
