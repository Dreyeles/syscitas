package com.sisol.sys_citas.controllerAdvices;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody; // Asegúrate de que esta importación exista
import jakarta.servlet.http.HttpServletRequest; // Importa esto
import org.springframework.web.servlet.resource.NoResourceFoundException; // Importa esto
import org.springframework.web.bind.MissingServletRequestParameterException; // Importa esto si manejas parámetros faltantes

import com.sisol.sys_citas.exceptions.AutenticacionException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Manejador específico para recursos no encontrados (ej. CSS, JS)
    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
        // Puedes loggear la excepción para depuración
        System.err.println("Recurso no encontrado: " + request.getRequestURI());
        // No redirigimos, Spring automáticamente intentará encontrar una página de error 404
        // o mostrará una página de error por defecto si no se configura una.
        // Si quieres una página 404 específica, podrías retornar "forward:/404"
        return null; // Permitir que Spring continúe su procesamiento de error (ej. WhiteLabelErrorPage)
    }

    // Manejador específico para MissingServletRequestParameterException (parámetros de URL faltantes)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParams(MissingServletRequestParameterException ex, RedirectAttributes ra) {
        String name = ex.getParameterName();
        System.err.println(name + " parámetro faltante.");
        ra.addFlashAttribute("errorGlobal", "Falta un parámetro requerido: " + name);
        return "redirect:/error"; // O a la página donde se espera el parámetro
    }

    // Para peticiones normales (no AJAX) - Manejador GENÉRICO de cualquier otra Exception
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, RedirectAttributes ra, HttpServletRequest request) {
        // Verifica si la solicitud es AJAX
        String requestedWith = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestedWith)) {
            // Si es AJAX, no redirigir. El controlador de AJAX debería manejar su propia respuesta de error.
            return null; // O podrías retornar una vista JSON si tu AJAX espera eso.
        }

        // Loguea la excepción para depuración
        ex.printStackTrace(); // Imprime el stack trace en la consola del servidor

        // Añade un mensaje de error a los atributos flash para que sea visible después de la redirección
        ra.addFlashAttribute("errorGlobal", "Ocurrió un error inesperado. Por favor, inténtalo de nuevo más tarde.");
        
        // Redirige a una página de error general o al inicio
        // Asegúrate de tener un controlador que mapee "/error"
        return "redirect:/error"; 
    }

    // Para excepciones de autenticación
    @ExceptionHandler(AutenticacionException.class)
    public String handleAuthException(AutenticacionException ex, RedirectAttributes ra, HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestedWith)) {
            return null;
        }
        ra.addFlashAttribute("error", ex.getMessage());
        return "redirect:/auth/login?error"; // Asumiendo que /auth/login es tu página de login
    }

    // Para errores de base de datos
    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    public String handleDataAccessException(org.springframework.dao.DataAccessException ex, RedirectAttributes ra, HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestedWith)) {
            return null;
        }
        ex.printStackTrace(); // Loguea el error de la DB
        ra.addFlashAttribute("error", "Error de conexión o de base de datos. Por favor, inténtalo más tarde.");
        return "redirect:/error"; // O una página de error específica de DB
    }
}