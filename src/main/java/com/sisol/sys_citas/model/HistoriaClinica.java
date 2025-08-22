package com.sisol.sys_citas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.sisol.sys_citas.enums.GrupoSanguineo;

@Entity
@Table(name = "historia_clinica")
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private Paciente paciente;

    @Enumerated(EnumType.STRING)
    @Column(name = "grupo_sanguineo", nullable = false)
    private GrupoSanguineo grupoSanguineo;

    @Column(name = "alergias", nullable = false, columnDefinition = "TEXT")
    private String alergias;

    @Column(name = "enfermedades_cronicas", nullable = false, columnDefinition = "TEXT")
    private String enfermedadesCronicas;

    @Column(name = "antecedentes_personales", nullable = false, columnDefinition = "TEXT")
    private String antecedentesPersonales;

    @Column(name = "antecedentes_familiares", nullable = false, columnDefinition = "TEXT")
    private String antecedentesFamiliares;

    @Column(name = "medicacion_actual", nullable = false, columnDefinition = "TEXT")
    private String medicacionActual;

    @Column(name = "discapacidad", nullable = false, columnDefinition = "TEXT")
    private String discapacidad;

    @Column(name = "vacunacion", nullable = false, columnDefinition = "TEXT")
    private String vacunacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Constructores
    public HistoriaClinica() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    public HistoriaClinica(Paciente paciente, GrupoSanguineo grupoSanguineo, String alergias,
                          String enfermedadesCronicas, String antecedentesPersonales,
                          String antecedentesFamiliares, String medicacionActual,
                          String discapacidad, String vacunacion) {
        this();
        this.paciente = paciente;
        this.grupoSanguineo = grupoSanguineo;
        this.alergias = alergias;
        this.enfermedadesCronicas = enfermedadesCronicas;
        this.antecedentesPersonales = antecedentesPersonales;
        this.antecedentesFamiliares = antecedentesFamiliares;
        this.medicacionActual = medicacionActual;
        this.discapacidad = discapacidad;
        this.vacunacion = vacunacion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public GrupoSanguineo getGrupoSanguineo() { return grupoSanguineo; }
    public void setGrupoSanguineo(GrupoSanguineo grupoSanguineo) { this.grupoSanguineo = grupoSanguineo; }

    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public String getEnfermedadesCronicas() { return enfermedadesCronicas; }
    public void setEnfermedadesCronicas(String enfermedadesCronicas) { this.enfermedadesCronicas = enfermedadesCronicas; }

    public String getAntecedentesPersonales() { return antecedentesPersonales; }
    public void setAntecedentesPersonales(String antecedentesPersonales) { this.antecedentesPersonales = antecedentesPersonales; }

    public String getAntecedentesFamiliares() { return antecedentesFamiliares; }
    public void setAntecedentesFamiliares(String antecedentesFamiliares) { this.antecedentesFamiliares = antecedentesFamiliares; }

    public String getMedicacionActual() { return medicacionActual; }
    public void setMedicacionActual(String medicacionActual) { this.medicacionActual = medicacionActual; }

    public String getDiscapacidad() { return discapacidad; }
    public void setDiscapacidad(String discapacidad) { this.discapacidad = discapacidad; }

    public String getVacunacion() { return vacunacion; }
    public void setVacunacion(String vacunacion) { this.vacunacion = vacunacion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
} 