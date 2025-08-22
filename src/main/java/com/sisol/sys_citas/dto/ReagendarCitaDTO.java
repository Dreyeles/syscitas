package com.sisol.sys_citas.dto;

public class ReagendarCitaDTO {
    private String numTicket;
    private Long nuevaDisponibilidadId;

    // Constructor vacío
    public ReagendarCitaDTO() {}

    // Constructor con parámetros
    public ReagendarCitaDTO(String numTicket, Long nuevaDisponibilidadId) {
        this.numTicket = numTicket;
        this.nuevaDisponibilidadId = nuevaDisponibilidadId;
    }

    // Getters y setters
    public String getNumTicket() {
        return numTicket;
    }

    public void setNumTicket(String numTicket) {
        this.numTicket = numTicket;
    }

    public Long getNuevaDisponibilidadId() {
        return nuevaDisponibilidadId;
    }

    public void setNuevaDisponibilidadId(Long nuevaDisponibilidadId) {
        this.nuevaDisponibilidadId = nuevaDisponibilidadId;
    }
}
