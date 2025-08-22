package com.sisol.sys_citas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import com.sisol.sys_citas.dto.UsuarioSesionDTO;
import com.sisol.sys_citas.model.Especialidad;
import com.sisol.sys_citas.model.Servicio;
import com.sisol.sys_citas.repository.EspecialidadRepository;
import com.sisol.sys_citas.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/servicios")
public class ServiciosController {

    @Autowired
    private EspecialidadRepository especialidadRepository;
    
    @Autowired
    private ServicioRepository servicioRepository;

    @GetMapping("")
    public String mostrarServicios(Model model, HttpSession session) {
        UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
        
        if (usuarioSesion == null) {
            return "redirect:/inicio?showLoginModal=true";
        }

        // Obtener todas las especialidades
        List<Especialidad> especialidades = especialidadRepository.findAll();
        
        // Obtener todos los servicios agrupados por especialidad
        Map<Long, List<Servicio>> serviciosPorEspecialidad = servicioRepository.findAllWithEspecialidad()
                .stream()
                .collect(Collectors.groupingBy(servicio -> servicio.getEspecialidad().getId()));

        model.addAttribute("usuario", usuarioSesion);
        model.addAttribute("titulo", "Servicios - SISOL");
        model.addAttribute("especialidades", especialidades);
        model.addAttribute("serviciosPorEspecialidad", serviciosPorEspecialidad);
        
        return "pages/paciente/servicios";
    }
} 