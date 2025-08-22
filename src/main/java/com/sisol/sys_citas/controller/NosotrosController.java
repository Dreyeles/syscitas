package com.sisol.sys_citas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import com.sisol.sys_citas.dto.UsuarioSesionDTO;

@Controller
@RequestMapping("/nosotros")
public class NosotrosController {

    @GetMapping("")
    public String mostrarNosotros(Model model, HttpSession session) {
        UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
        
        if (usuarioSesion == null) {
            return "redirect:/inicio?showLoginModal=true";
        }

        model.addAttribute("usuario", usuarioSesion);
        model.addAttribute("titulo", "Nosotros - SISOL");
        
        return "pages/paciente/nosotros";
    }
} 