package com.sisol.sys_citas.dto;

import jakarta.validation.constraints.*;
import java.time.LocalTime;
import java.time.LocalDate;

public class RegistroMedicoDTO {

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden exceder los 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden exceder los 100 caracteres")
    private String apellidos;

    @NotBlank(message = "El número de colegiado es obligatorio")
    @Size(min = 1, max = 6, message = "El número de colegiado debe tener entre 1 y 6 caracteres")
    private String numeroColegiado;

    @NotNull(message = "El turno es obligatorio")
    private String turno;

    @NotNull(message = "La hora de inicio del turno es obligatoria")
    private LocalTime horaInicioTurno;

    @NotNull(message = "La hora de fin del turno es obligatoria")
    private LocalTime horaFinTurno;

    @NotNull(message = "La especialidad es obligatoria")
    private Long especialidadId;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener exactamente 8 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "El DNI solo debe contener números")
    private String dni;

    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    private String email;

    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    private String telefono;

    @NotNull(message = "El estado es obligatorio")
    private String estado;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "La confirmación de contraseña es obligatoria")
    private String confirmPassword;

    @NotNull(message = "El sexo es obligatorio")
    private String sexo; // values: MASCULINO, FEMENINO, OTRO

    @NotNull(message = "El estado civil es obligatorio")
    private String estadoCivil; // values persisted as SOLTERO/A, CASADO/A, ...

    private LocalDate fechaNacimiento; // opcional

    // --- Getters y Setters ---
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getNumeroColegiado() { return numeroColegiado; }
    public void setNumeroColegiado(String numeroColegiado) { this.numeroColegiado = numeroColegiado; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public LocalTime getHoraInicioTurno() { return horaInicioTurno; }
    public void setHoraInicioTurno(LocalTime horaInicioTurno) { this.horaInicioTurno = horaInicioTurno; }

    public LocalTime getHoraFinTurno() { return horaFinTurno; }
    public void setHoraFinTurno(LocalTime horaFinTurno) { this.horaFinTurno = horaFinTurno; }

    public Long getEspecialidadId() { return especialidadId; }
    public void setEspecialidadId(Long especialidadId) { this.especialidadId = especialidadId; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
} 