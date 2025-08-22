package com.sisol.sys_citas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import com.sisol.sys_citas.dto.UsuarioSesionDTO;
import com.sisol.sys_citas.dto.AgregarEspecialidadDTO;
import com.sisol.sys_citas.dto.AgregarServicioDTO;
import com.sisol.sys_citas.dto.RegistroMedicoDTO;
import com.sisol.sys_citas.dto.RegistroPacienteDTO;
import com.sisol.sys_citas.enums.Rol;
import com.sisol.sys_citas.repository.EspecialidadRepository;
import com.sisol.sys_citas.repository.MedicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/dashboard-administrativo")
public class DashboardAdministrativoController {
    private final EspecialidadRepository especialidadRepository;
    private final MedicoRepository medicoRepository;

    public DashboardAdministrativoController(EspecialidadRepository especialidadRepository,
                                            MedicoRepository medicoRepository) {
        this.especialidadRepository = especialidadRepository;
        this.medicoRepository = medicoRepository;
    }
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardAdministrativoController.class);
    
    @GetMapping("")
    public String mostrarDashboard(Model model, HttpSession session) {
        logger.info("Accediendo al dashboard administrativo");
        
        // Verificar que el usuario esté autenticado y tenga el rol correcto
        UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
        
        if (usuarioSesion == null) {
            logger.warn("No hay usuario en sesión, redirigiendo al login");
            return "redirect:/inicio?showLoginModal=true";
        }
        
        if (usuarioSesion.getRol() != Rol.ROLE_PERSONAL_ADMINISTRATIVO) {
            logger.warn("Usuario sin permisos de administrador, redirigiendo al inicio");
            return "redirect:/inicio";
        }
        
        logger.info("Usuario administrativo autenticado: {}", usuarioSesion.getCorreo());
        
        // Agregar datos al modelo
        model.addAttribute("usuario", usuarioSesion);
        model.addAttribute("titulo", "Dashboard Administrativo - SISOL");
        model.addAttribute("especialidades", especialidadRepository.findAll());
        model.addAttribute("sexos", com.sisol.sys_citas.enums.Sexo.values());
        model.addAttribute("estadosCiviles", com.sisol.sys_citas.enums.EstadoCivil.values());
        // Lista completa de médicos con especialidad cargada
        model.addAttribute("medicos", medicoRepository.findAllWithEspecialidad());

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
        
        return "dashboard";
    }
    

} 