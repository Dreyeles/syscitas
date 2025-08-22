package com.sisol.sys_citas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CambiarPasswordDTO {

    @NotBlank(message = "La contraseña actual es obligatoria")
    private String passwordActual;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String passwordNueva;

    @NotBlank(message = "Debe confirmar la nueva contraseña")
    private String passwordConfirmar;

    // Constructores
    public CambiarPasswordDTO() {
    }

    public CambiarPasswordDTO(String passwordActual, String passwordNueva, String passwordConfirmar) {
        this.passwordActual = passwordActual;
        this.passwordNueva = passwordNueva;
        this.passwordConfirmar = passwordConfirmar;
    }

    // Getters y Setters
    public String getPasswordActual() {
        return passwordActual;
    }

    public void setPasswordActual(String passwordActual) {
        this.passwordActual = passwordActual;
    }

    public String getPasswordNueva() {
        return passwordNueva;
    }

    public void setPasswordNueva(String passwordNueva) {
        this.passwordNueva = passwordNueva;
    }

    public String getPasswordConfirmar() {
        return passwordConfirmar;
    }

    public void setPasswordConfirmar(String passwordConfirmar) {
        this.passwordConfirmar = passwordConfirmar;
    }

    // Método para validar que las contraseñas coincidan
    public boolean passwordsCoinciden() {
        return passwordNueva != null && passwordNueva.equals(passwordConfirmar);
    }

    @Override
    public String toString() {
        return "CambiarPasswordDTO{" +
                "passwordActual='" + (passwordActual != null ? "[PROTECTED]" : "null") + '\'' +
                ", passwordNueva='" + (passwordNueva != null ? "[PROTECTED]" : "null") + '\'' +
                ", passwordConfirmar='" + (passwordConfirmar != null ? "[PROTECTED]" : "null") + '\'' +
                '}';
    }
} 