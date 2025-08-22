package com.sisol.sys_citas.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RegistroPacienteDTO {
    
    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres")
    private String nombres;
    
    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
    private String apellidos;
    
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe tener 8 dígitos")
    private String dni;
    
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;
    
    @NotBlank(message = "El sexo es obligatorio")
    private String sexo;
    
    @NotBlank(message = "El estado civil es obligatorio")
    private String estadoCivil;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
    
    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    private String direccion;
    
    @Size(max = 100, message = "El distrito no puede exceder 100 caracteres")
    private String distrito;
    
    @NotBlank(message = "El nombre del contacto de emergencia es obligatorio")
    @Size(max = 100, message = "El nombre del contacto no puede exceder 100 caracteres")
    private String contactoEmergenciaNombre;
    
    @NotBlank(message = "El teléfono de emergencia es obligatorio")
    @Pattern(regexp = "^[0-9]{9,11}$", message = "El teléfono debe tener entre 9 y 11 dígitos")
    private String contactoEmergenciaTelefono;
    
    @NotBlank(message = "El parentesco es obligatorio")
    @Size(max = 20, message = "El parentesco no puede exceder 20 caracteres")
    private String contactoEmergenciaParentesco;
}
