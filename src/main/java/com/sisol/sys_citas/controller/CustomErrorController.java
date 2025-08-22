package com.sisol.sys_citas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

@Controller
public class CustomErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute("javax.servlet.error.status_code");
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("errorCode", "403");
                model.addAttribute("errorTitle", "Acceso Denegado");
                model.addAttribute("errorMessage", "No tienes permisos para acceder a esta página.");
                return "error/403";
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorCode", "404");
                model.addAttribute("errorTitle", "Página No Encontrada");
                model.addAttribute("errorMessage", "La página que buscas no existe.");
                return "error/404";
            }
        }
        
        model.addAttribute("errorCode", "500");
        model.addAttribute("errorTitle", "Error del Servidor");
        model.addAttribute("errorMessage", "Ha ocurrido un error interno.");
        return "error/500";
    }
} 