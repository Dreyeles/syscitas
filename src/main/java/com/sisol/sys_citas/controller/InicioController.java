package com.sisol.sys_citas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class InicioController {
    
    @GetMapping("/")
    public String mostrarInicio(@RequestParam(name = "showLoginModal", required = false, defaultValue = "false") boolean showLoginModal,
                                Model model,
                                HttpSession session){
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("titulo", "SISOL - Sistema de Citas Médicas");
        model.addAttribute("descripcion", "Sistema Integral de Salud Ocupacional y Laboral");
        model.addAttribute("openLoginModal", showLoginModal);
        return "inicio";
    }
    
    @GetMapping("/inicio")
    public String mostrarInicioAlternativo(@RequestParam(name = "showLoginModal", required = false, defaultValue = "false") boolean showLoginModal,
                                           Model model,
                                           HttpSession session){
        // Ahora llamamos al método principal pasando todos los parámetros
        return mostrarInicio(showLoginModal, model, session);
    }

    @ModelAttribute
    public void agregarUsuarioAlModelo(Model model, HttpSession session) {
        Object usuario = session.getAttribute("usuario");
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
        }
    }
}