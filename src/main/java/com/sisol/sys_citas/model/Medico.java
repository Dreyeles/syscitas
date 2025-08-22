package com.sisol.sys_citas.model;

import jakarta.persistence.*;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "medico")
public class Medico {

    @Id
    private Long id;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "numero_colegiado", nullable = false, length = 6)
    private String numeroColegiado;

    @Enumerated(EnumType.STRING)
    @Column(name = "turno", nullable = false)
    private TurnoMedico turno;

    @Column(name = "hora_inicio_turno", nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicioTurno;

    @Column(name = "hora_fin_turno", nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFinTurno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidad_id", nullable = false)
    private Especialidad especialidad;

    @Column(name = "dni", nullable = false, length = 8)
    private String dni;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "hora_inicio_atencion")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicioAtencion;

    @Column(name = "hora_fin_atencion")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFinAtencion;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoMedico estado = EstadoMedico.ACTIVO;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", nullable = false)
    private com.sisol.sys_citas.enums.Sexo sexo;

    @Convert(converter = com.sisol.sys_citas.persistence.converter.EstadoCivilConverter.class)
    @Column(name = "estado_civil", nullable = false, length = 20)
    private com.sisol.sys_citas.enums.EstadoCivil estadoCivil;

    @Column(name = "fecha_nacimiento")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.time.LocalDate fechaNacimiento;

    // Relación OneToOne con Usuario - IGNORAR en serialización JSON
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    @MapsId
    @JsonIgnore
    private Usuario usuario;

    // --- Constructores ---
    public Medico() {}

    // Constructor con campos básicos
    public Medico(String nombres, String apellidos, String numeroColegiado, TurnoMedico turno, 
                  LocalTime horaInicioTurno, LocalTime horaFinTurno, Especialidad especialidad, 
                  String dni, String email, LocalTime horaInicioAtencion, LocalTime horaFinAtencion, 
                  String telefono, EstadoMedico estado, Usuario usuario) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.numeroColegiado = numeroColegiado;
        this.turno = turno;
        this.horaInicioTurno = horaInicioTurno;
        this.horaFinTurno = horaFinTurno;
        this.especialidad = especialidad;
        this.dni = dni;
        this.email = email;
        this.horaInicioAtencion = horaInicioAtencion;
        this.horaFinAtencion = horaFinAtencion;
        this.telefono = telefono;
        this.estado = estado;
        this.usuario = usuario;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    
    public String getNumeroColegiado() { return numeroColegiado; }
    public void setNumeroColegiado(String numeroColegiado) { this.numeroColegiado = numeroColegiado; }
    
    public TurnoMedico getTurno() { return turno; }
    public void setTurno(TurnoMedico turno) { this.turno = turno; }
    
    public LocalTime getHoraInicioTurno() { return horaInicioTurno; }
    public void setHoraInicioTurno(LocalTime horaInicioTurno) { this.horaInicioTurno = horaInicioTurno; }
    
    public LocalTime getHoraFinTurno() { return horaFinTurno; }
    public void setHoraFinTurno(LocalTime horaFinTurno) { this.horaFinTurno = horaFinTurno; }
    
    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }
    
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalTime getHoraInicioAtencion() { return horaInicioAtencion; }
    public void setHoraInicioAtencion(LocalTime horaInicioAtencion) { this.horaInicioAtencion = horaInicioAtencion; }
    
    public LocalTime getHoraFinAtencion() { return horaFinAtencion; }
    public void setHoraFinAtencion(LocalTime horaFinAtencion) { this.horaFinAtencion = horaFinAtencion; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public EstadoMedico getEstado() { return estado; }
    public void setEstado(EstadoMedico estado) { this.estado = estado; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public com.sisol.sys_citas.enums.Sexo getSexo() { return sexo; }
    public void setSexo(com.sisol.sys_citas.enums.Sexo sexo) { this.sexo = sexo; }

    public com.sisol.sys_citas.enums.EstadoCivil getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(com.sisol.sys_citas.enums.EstadoCivil estadoCivil) { this.estadoCivil = estadoCivil; }

    public java.time.LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(java.time.LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    // Enums internos con serialización JSON mejorada
    public enum TurnoMedico {
        MANANA("Mañana"),
        TARDE("Tarde");
        
        private final String displayName;
        
        TurnoMedico(String displayName) {
            this.displayName = displayName;
        }
        
        @JsonValue
        public String getDisplayName() {
            return displayName;
        }
        
        public String getValue() {
            return this.name();
        }
    }

    public enum EstadoMedico {
        ACTIVO("Activo"),
        INACTIVO("Inactivo"),
        LICENCIA("Licencia"),
        VACACIONES("Vacaciones");
        
        private final String displayName;
        
        EstadoMedico(String displayName) {
            this.displayName = displayName;
        }
        
        @JsonValue
        public String getDisplayName() {
            return displayName;
        }
        
        public String getValue() {
            return this.name();
        }
    }
}