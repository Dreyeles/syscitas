package com.sisol.sys_citas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import com.sisol.sys_citas.dto.UsuarioSesionDTO;

@Controller
@RequestMapping("/citas")
public class CitasController {
    
    @GetMapping("/nueva")
    public String mostrarFormularioCita(Model model, HttpSession session){
        UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
        
        if (usuarioSesion == null) {
            return "redirect:/inicio?showLoginModal=true";
        }
        
        model.addAttribute("titulo", "Agendar Nueva Cita - SISOL");
        model.addAttribute("usuario", usuarioSesion);
        return "pages/paciente/citas-nueva";
    }
    
    @GetMapping("/pagar")
    public String mostrarPaginaPago(HttpSession session, Model model) {
        // Verificar si el usuario está logueado
        UsuarioSesionDTO usuario = (UsuarioSesionDTO) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/inicio?showLoginModal=true";
        }
        
        model.addAttribute("titulo", "Procesar Pago - SISOL");
        model.addAttribute("usuario", usuario);
        return "pages/paciente/pagar";
    }
    
    @PostMapping("/nueva")
    public String procesarCita(
            @RequestParam String departamento,
            @RequestParam String doctor,
            @RequestParam String fecha,
            @RequestParam String hora,
            Model model,
            HttpSession session){
        
        // Aquí iría la lógica para guardar la cita
        // Por ahora solo redirigimos a la página de inicio
        model.addAttribute("mensaje", "Cita agendada exitosamente");
        return "redirect:/";
    }
    

} 