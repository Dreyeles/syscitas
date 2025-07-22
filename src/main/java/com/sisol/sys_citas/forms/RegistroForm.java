package com.sisol.sys_citas.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegistroForm {

    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombres;

    @NotEmpty(message = "El correo no puede estar vacío")
    @Email(message = "Debe ser un formato de correo válido")
    private String correo;

    @NotEmpty(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotEmpty(message = "Debe repetir la contraseña")
    private String repetirPassword;

    // Getters y Setters
    // Puedes generarlos automáticamente en tu IDE (IntelliJ, Eclipse, VS Code)
    // haciendo clic derecho > Generate > Getters and Setters
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRepetirPassword() { return repetirPassword; }
    public void setRepetirPassword(String repetirPassword) { this.repetirPassword = repetirPassword; }
}