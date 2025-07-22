package com.sisol.sys_citas.exceptions;

public class RegistroException extends RuntimeException {

    public RegistroException(String message) {
        super(message);
    }

    // Puedes añadir constructores adicionales si necesitas manejar causas (Throwable cause)
    public RegistroException(String message, Throwable cause) {
        super(message, cause);
    }
}