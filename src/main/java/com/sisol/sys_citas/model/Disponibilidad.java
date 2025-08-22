package com.sisol.sys_citas.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilidad")
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(name = "fecha_disponibilidad", nullable = false)
    private LocalDate fechaDisponibilidad;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "disponible", nullable = false)
    private Boolean disponible = true;

    @Column(name = "num_ticket", nullable = false)
    private Integer numTicket;

    // --- Constructores ---
    public Disponibilidad() {}

    public Disponibilidad(Medico medico, LocalDate fechaDisponibilidad, LocalTime horaInicio, LocalTime horaFin, Integer numTicket) {
        this.medico = medico;
        this.fechaDisponibilidad = fechaDisponibilidad;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.disponible = true;
        this.numTicket = numTicket;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public LocalDate getFechaDisponibilidad() { return fechaDisponibilidad; }
    public void setFechaDisponibilidad(LocalDate fechaDisponibilidad) { this.fechaDisponibilidad = fechaDisponibilidad; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }

    public Integer getNumTicket() { return numTicket; }
    public void setNumTicket(Integer numTicket) { this.numTicket = numTicket; }
} 