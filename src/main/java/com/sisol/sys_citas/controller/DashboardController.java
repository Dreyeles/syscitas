package com.sisol.sys_citas.controller;

import com.sisol.sys_citas.dto.AgregarEspecialidadDTO;
import com.sisol.sys_citas.dto.AgregarServicioDTO;
import com.sisol.sys_citas.dto.RegistroMedicoDTO;
import com.sisol.sys_citas.dto.RegistroPacienteDTO;
import com.sisol.sys_citas.dto.UsuarioSesionDTO;
import com.sisol.sys_citas.repository.EspecialidadRepository;
import com.sisol.sys_citas.repository.MedicoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final EspecialidadRepository especialidadRepository;
    private final MedicoRepository medicoRepository;

    public DashboardController(EspecialidadRepository especialidadRepository,
                               MedicoRepository medicoRepository) {
        this.especialidadRepository = especialidadRepository;
        this.medicoRepository = medicoRepository;
    }
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    @GetMapping("")
    public String mostrarDashboard(Model model, HttpSession session) {
        logger.info("Accediendo al dashboard");
        
        // Verificar que el usuario esté autenticado
        UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
        
        if (usuarioSesion == null) {
            logger.warn("No hay usuario en sesión, redirigiendo al login");
            return "redirect:/inicio?showLoginModal=true";
        }
        
        logger.info("Usuario autenticado: {} con rol: {}", usuarioSesion.getCorreo(), usuarioSesion.getRol());
        
        // Agregar datos básicos al modelo para todos los usuarios
        model.addAttribute("usuario", usuarioSesion);
        
        // Solo agregar datos administrativos si el usuario es administrador
        if ("ROLE_PERSONAL_ADMINISTRATIVO".equals(usuarioSesion.getRol().name())) {
            logger.info("Usuario es administrador, cargando datos administrativos");
            
            // Datos para administradores
            model.addAttribute("especialidades", especialidadRepository.findAll());
            model.addAttribute("sexos", com.sisol.sys_citas.enums.Sexo.values());
            model.addAttribute("estadosCiviles", com.sisol.sys_citas.enums.EstadoCivil.values());
            model.addAttribute("medicos", medicoRepository.findAllWithEspecialidad());
            
            // DTOs para formularios administrativos
            if (!model.containsAttribute("agregarEspecialidadDTO")) {
                model.addAttribute("agregarEspecialidadDTO", new AgregarEspecialidadDTO());
            }
            if (!model.containsAttribute("registroMedicoDTO")) {
                model.addAttribute("registroMedicoDTO", new RegistroMedicoDTO());
            }
            if (!model.containsAttribute("agregarServicioDTO")) {
                model.addAttribute("agregarServicioDTO", new AgregarServicioDTO());
            }
            if (!model.containsAttribute("registroPacienteDTO")) {
                model.addAttribute("registroPacienteDTO", new RegistroPacienteDTO());
            }
            
            // Lista de parentescos para el formulario de pacientes
            List<String> parentescos = Arrays.asList(
                "Cónyuge", "Padre", "Madre", "Hijo/a", "Hermano/a", "Amigo/a", "Otro"
            );
            model.addAttribute("parentescos", parentescos);
            
        } else if ("ROLE_MEDICO".equals(usuarioSesion.getRol().name())) {
            logger.info("Usuario es médico, cargando solo datos médicos básicos");
            
            // Para médicos solo agregar datos básicos necesarios
            model.addAttribute("sexos", com.sisol.sys_citas.enums.Sexo.values());
            model.addAttribute("estadosCiviles", com.sisol.sys_citas.enums.EstadoCivil.values());
            
        } else if ("ROLE_PACIENTE".equals(usuarioSesion.getRol().name())) {
            logger.info("Usuario es paciente, cargando solo datos básicos");
            
            // Para pacientes solo agregar datos básicos necesarios
            model.addAttribute("sexos", com.sisol.sys_citas.enums.Sexo.values());
            model.addAttribute("estadosCiviles", com.sisol.sys_citas.enums.EstadoCivil.values());
        }
        
        return "dashboard";
    }
} 