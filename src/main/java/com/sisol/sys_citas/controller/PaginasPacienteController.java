package com.sisol.sys_citas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import com.sisol.sys_citas.dto.UsuarioSesionDTO;
import com.sisol.sys_citas.dto.ActualizarPacienteDTO;
import com.sisol.sys_citas.model.Paciente;
import com.sisol.sys_citas.model.Usuario;
import com.sisol.sys_citas.repository.PacienteRepository;
import com.sisol.sys_citas.repository.UsuarioRepository;
import com.sisol.sys_citas.service.ActualizarPacienteService;
import com.sisol.sys_citas.service.CambiarPasswordService;
import com.sisol.sys_citas.dto.CambiarPasswordDTO;
import com.sisol.sys_citas.enums.EstadoCivil;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Controller
public class PaginasPacienteController {

    private static final Logger logger = LoggerFactory.getLogger(PaginasPacienteController.class);

    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ActualizarPacienteService actualizarPacienteService;
    
    @Autowired
    private CambiarPasswordService cambiarPasswordService;

    @GetMapping("/mi-cuenta")
    public String mostrarMiCuenta(Model model, HttpSession session) {
        logger.info("Accediendo a /mi-cuenta");
        
        // Obtener el usuario de la sesión
        UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
        
        if (usuarioSesion == null) {
            logger.warn("No hay usuario en sesión, redirigiendo al login");
            return "redirect:/inicio?showLoginModal=true";
        }

        logger.info("Usuario en sesión: {}", usuarioSesion.getCorreo());
        logger.info("ID del usuario: {}", usuarioSesion.getId());

        // Buscar los datos completos del paciente y usuario
        Paciente paciente = pacienteRepository.findById(usuarioSesion.getId()).orElse(null);
        Usuario usuario = usuarioRepository.findById(usuarioSesion.getId()).orElse(null);
        
        if (paciente == null || usuario == null) {
            logger.error("No se encontró paciente o usuario con ID: {}", usuarioSesion.getId());
            return "redirect:/inicio?showLoginModal=true";
        }

        logger.info("Paciente encontrado: {} {}", paciente.getNombres(), paciente.getApellidos());
        logger.info("Usuario encontrado con fecha de registro: {}", usuario.getFechaRegistro());

        // Crear DTO con los datos actuales del paciente
        ActualizarPacienteDTO actualizarDTO = new ActualizarPacienteDTO();
        actualizarDTO.setCorreo(usuarioSesion.getCorreo());
        actualizarDTO.setNombres(paciente.getNombres());
        actualizarDTO.setApellidos(paciente.getApellidos());
        actualizarDTO.setTelefono(paciente.getTelefono());
        actualizarDTO.setDireccion(paciente.getDireccion());
        actualizarDTO.setEstadoCivil(paciente.getEstadoCivil());
        actualizarDTO.setContactoEmergenciaNombre(paciente.getContactoEmergenciaNombre());
        actualizarDTO.setContactoEmergenciaTelefono(paciente.getContactoEmergenciaTelefono());

        // Agregar los datos al modelo
        model.addAttribute("paciente", paciente);
        model.addAttribute("usuario", usuarioSesion);
        model.addAttribute("usuarioCompleto", usuario);
        model.addAttribute("actualizarPacienteDTO", actualizarDTO);
        model.addAttribute("cambiarPasswordDTO", new CambiarPasswordDTO());
        model.addAttribute("estadosCiviles", EstadoCivil.values());
        model.addAttribute("titulo", "Mi Cuenta - SISOL");
        
        logger.info("Renderizando página mi-cuenta");
        return "pages/paciente/mi-cuenta";
    }

    @PostMapping("/mi-cuenta/actualizar")
    public String actualizarDatosPaciente(
            @Valid @ModelAttribute("actualizarPacienteDTO") ActualizarPacienteDTO actualizarDTO,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        logger.info("Iniciando actualización de datos del paciente");
        
        // Obtener el usuario de la sesión
        UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
        
        if (usuarioSesion == null) {
            logger.warn("No hay usuario en sesión durante actualización");
            return "redirect:/inicio?showLoginModal=true";
        }

        // Si hay errores de validación, volver a mostrar el formulario
        if (bindingResult.hasErrors()) {
            logger.warn("Errores de validación en el formulario");
            
            // Recargar los datos necesarios para el formulario
            Paciente paciente = pacienteRepository.findById(usuarioSesion.getId()).orElse(null);
            Usuario usuario = usuarioRepository.findById(usuarioSesion.getId()).orElse(null);
            if (paciente != null && usuario != null) {
                model.addAttribute("paciente", paciente);
                model.addAttribute("usuario", usuarioSesion);
                model.addAttribute("usuarioCompleto", usuario);
                model.addAttribute("cambiarPasswordDTO", new CambiarPasswordDTO());
                model.addAttribute("estadosCiviles", EstadoCivil.values());
                model.addAttribute("titulo", "Mi Cuenta - SISOL");
            }
            
            return "pages/paciente/mi-cuenta";
        }

        try {
            // Actualizar datos usando el servicio
            Paciente pacienteActualizado = actualizarPacienteService.actualizarDatosPaciente(
                usuarioSesion.getId(), actualizarDTO);
            
            // Actualizar la sesión con los nuevos datos
            usuarioSesion.setCorreo(actualizarDTO.getCorreo());
            usuarioSesion.setNombres(actualizarDTO.getNombres());
            usuarioSesion.setApellidos(actualizarDTO.getApellidos());
            session.setAttribute("usuario", usuarioSesion);
            
            logger.info("Datos del paciente actualizados exitosamente para ID: {}", usuarioSesion.getId());
            redirectAttributes.addFlashAttribute("mensajeExito", "Datos actualizados exitosamente");
            
        } catch (Exception e) {
            logger.error("Error al actualizar datos del paciente: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al actualizar los datos: " + e.getMessage());
        }
        
        return "redirect:/mi-cuenta";
    }

    @PostMapping("/mi-cuenta/cambiar-password")
    public String cambiarPassword(
            @Valid @ModelAttribute("cambiarPasswordDTO") CambiarPasswordDTO cambiarPasswordDTO,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        logger.info("Iniciando cambio de contraseña");
        
        // Obtener el usuario de la sesión
        UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
        
        if (usuarioSesion == null) {
            logger.warn("No hay usuario en sesión durante cambio de contraseña");
            return "redirect:/inicio?showLoginModal=true";
        }

        // Si hay errores de validación, volver a mostrar la página
        if (bindingResult.hasErrors()) {
            logger.warn("Errores de validación en el formulario de cambio de contraseña");
            redirectAttributes.addFlashAttribute("error", "Por favor corrija los errores en el formulario");
            return "redirect:/mi-cuenta";
        }

        try {
            // Cambiar contraseña usando el servicio
            cambiarPasswordService.cambiarPassword(usuarioSesion.getId(), cambiarPasswordDTO);
            
            logger.info("Contraseña cambiada exitosamente para usuario ID: {}", usuarioSesion.getId());
            redirectAttributes.addFlashAttribute("mensajeExito", "Contraseña cambiada exitosamente");
            
        } catch (Exception e) {
            logger.error("Error al cambiar contraseña: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al cambiar la contraseña: " + e.getMessage());
        }
        
        return "redirect:/mi-cuenta";
    }
} 