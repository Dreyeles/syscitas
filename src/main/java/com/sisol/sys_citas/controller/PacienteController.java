package com.sisol.sys_citas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.sisol.sys_citas.dto.RegistroPacienteDTO;
import com.sisol.sys_citas.dto.PacienteListaDTO;
import com.sisol.sys_citas.dto.UsuarioSesionDTO;
import com.sisol.sys_citas.service.PacienteService;
import com.sisol.sys_citas.model.Paciente;
import com.sisol.sys_citas.repository.PacienteRepository;
import com.sisol.sys_citas.enums.Rol;
import com.sisol.sys_citas.enums.Sexo;
import com.sisol.sys_citas.enums.EstadoCivil;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);
    
    private final PacienteService pacienteService;
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }
    
    @GetMapping("/registrar")
    public String mostrarFormularioRegistro(Model model) {
        // Esta ruta no debería ser accesible directamente
        // Redirigir al dashboard
        return "redirect:/dashboard?tab=añadir-paciente";
    }
    
    @PostMapping("/registrar")
    public String registrarPaciente(@Valid @ModelAttribute("registroPacienteDTO") RegistroPacienteDTO dto,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registroPacienteDTO", result);
            redirectAttributes.addFlashAttribute("registroPacienteDTO", dto);
            redirectAttributes.addFlashAttribute("mensajeError", "Por favor corrija los errores en el formulario");
            return "redirect:/dashboard?tab=añadir-paciente";
        }
        
        try {
            pacienteService.registrarPaciente(dto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Paciente registrado exitosamente");
            return "redirect:/dashboard?tab=añadir-paciente&ok=1";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("registroPacienteDTO", dto);
            redirectAttributes.addFlashAttribute("mensajeError", "Error al registrar paciente: " + e.getMessage());
            return "redirect:/dashboard?tab=añadir-paciente";
        }
    }
    
    // Endpoint REST para obtener todos los pacientes en formato JSON
    @GetMapping("/api/listar")
    @ResponseBody
    public ResponseEntity<List<PacienteListaDTO>> listarPacientes(HttpSession session) {
        logger.info("Obteniendo lista de pacientes");
        
        try {
            // Verificar que el usuario esté autenticado y tenga el rol correcto
            UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
            
            if (usuarioSesion == null) {
                logger.warn("Usuario no autenticado");
                return ResponseEntity.status(401).build();
            }
            
            if (usuarioSesion.getRol() != Rol.ROLE_PERSONAL_ADMINISTRATIVO) {
                logger.warn("Usuario sin permisos para acceder a lista de pacientes");
                return ResponseEntity.status(403).build();
            }
            
            // Obtener pacientes de la base de datos
            List<Paciente> pacientes = pacienteRepository.findAll();
            logger.info("Encontrados {} pacientes", pacientes.size());
            
            // Convertir a DTO
            List<PacienteListaDTO> pacientesDTO = pacientes.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(pacientesDTO);
        } catch (Exception e) {
            logger.error("Error al obtener lista de pacientes: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Método auxiliar para convertir Paciente a PacienteListaDTO
    private PacienteListaDTO convertirADTO(Paciente paciente) {
        return new PacienteListaDTO(
            paciente.getId(),
            paciente.getDni(),
            paciente.getNombres(),
            paciente.getApellidos(),
            paciente.getTelefono(),
            paciente.getEstadoCivil() != null ? paciente.getEstadoCivil().name() : null,
            paciente.getDireccion(),
            paciente.getDistrito(),
            paciente.getContactoEmergenciaNombre(),
            paciente.getContactoEmergenciaTelefono(),
            paciente.getContactoEmergenciaParentesco(),
            paciente.getFechaNacimiento(),
            paciente.getSexo() != null ? paciente.getSexo().name() : null
        );
    }
}
