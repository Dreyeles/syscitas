package com.sisol.sys_citas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime; // Para la fecha y hora de la cita

@Entity
@Table(name = "cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente; // Asegúrate de tener la entidad Paciente

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico; // Asegúrate de tener la entidad Medico

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio; // Asegúrate de tener la entidad Servicio

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado; // Por ejemplo: "PENDIENTE", "CONFIRMADA", "CANCELADA", "COMPLETADA"

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    // --- Constructores ---
    public Cita() {}

    public Cita(Paciente paciente, Medico medico, Servicio servicio, LocalDateTime fechaHora, String estado, String observaciones) {
        this.paciente = paciente;
        this.medico = medico;
        this.servicio = servicio;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}