package com.sisol.sys_citas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import com.sisol.sys_citas.dto.UsuarioSesionDTO;
import com.sisol.sys_citas.enums.Rol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/dashboard-medico")
public class DashboardMedicoController {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardMedicoController.class);
    
    @GetMapping("")
    public String mostrarDashboard(Model model, HttpSession session) {
        logger.info("Accediendo al dashboard médico");
        
        // Verificar que el usuario esté autenticado y tenga el rol correcto
        UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
        
        if (usuarioSesion == null) {
            logger.warn("No hay usuario en sesión, redirigiendo al login");
            return "redirect:/inicio?showLoginModal=true";
        }
        
        if (usuarioSesion.getRol() != Rol.ROLE_MEDICO) {
            logger.warn("Usuario sin permisos de médico, redirigiendo al inicio");
            return "redirect:/inicio";
        }
        
        logger.info("Usuario médico autenticado: {}", usuarioSesion.getCorreo());
        
        // Agregar datos al modelo
        model.addAttribute("usuario", usuarioSesion);
        model.addAttribute("titulo", "Dashboard Médico - SISOL");
        
        return "dashboard";
    }
} 