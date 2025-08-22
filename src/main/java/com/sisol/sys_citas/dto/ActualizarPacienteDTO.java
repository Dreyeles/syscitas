package com.sisol.sys_citas.dto;

import com.sisol.sys_citas.enums.EstadoCivil;
import jakarta.validation.constraints.*;

public class ActualizarPacienteDTO {

    // Datos de Usuario (para la tabla 'usuario')
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo es inválido")
    @Size(max = 100, message = "El correo no puede exceder los 100 caracteres")
    private String correo;

    // Datos de Paciente (para la tabla 'paciente')
    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden exceder los 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden exceder los 100 caracteres")
    private String apellidos;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos numéricos")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255, message = "La dirección no puede exceder los 255 caracteres")
    private String direccion;

    @NotBlank(message = "El distrito es obligatorio")
    @Size(max = 100, message = "El distrito no puede exceder los 100 caracteres")
    private String distrito;

    @NotNull(message = "El estado civil es obligatorio")
    private EstadoCivil estadoCivil;

    @NotBlank(message = "El nombre de contacto de emergencia es obligatorio")
    @Size(max = 100, message = "El nombre de contacto de emergencia no puede exceder los 100 caracteres")
    private String contactoEmergenciaNombre;

    @NotBlank(message = "El teléfono de contacto de emergencia es obligatorio")
    @Pattern(regexp = "\\d{9}", message = "El teléfono de contacto de emergencia debe tener 9 dígitos numéricos")
    private String contactoEmergenciaTelefono;

    @NotBlank(message = "El parentesco del contacto de emergencia es obligatorio")
    @Size(max = 20, message = "El parentesco no puede exceder los 20 caracteres")
    private String contactoEmergenciaParentesco;

    // --- Getters y Setters ---
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }

    public EstadoCivil getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(EstadoCivil estadoCivil) { this.estadoCivil = estadoCivil; }
    
    public String getContactoEmergenciaNombre() { return contactoEmergenciaNombre; }
    public void setContactoEmergenciaNombre(String contactoEmergenciaNombre) { this.contactoEmergenciaNombre = contactoEmergenciaNombre; }
    
    public String getContactoEmergenciaTelefono() { return contactoEmergenciaTelefono; }
    public void setContactoEmergenciaTelefono(String contactoEmergenciaTelefono) { this.contactoEmergenciaTelefono = contactoEmergenciaTelefono; }

    public String getContactoEmergenciaParentesco() { return contactoEmergenciaParentesco; }
    public void setContactoEmergenciaParentesco(String contactoEmergenciaParentesco) { this.contactoEmergenciaParentesco = contactoEmergenciaParentesco; }
} 