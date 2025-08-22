package com.sisol.sys_citas.controller;

import com.sisol.sys_citas.dto.AgregarEspecialidadDTO;
import com.sisol.sys_citas.service.AgregarEspecialidadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

// Agregar imports necesarios
import jakarta.servlet.http.HttpSession;
import com.sisol.sys_citas.dto.UsuarioSesionDTO;
import com.sisol.sys_citas.enums.Rol;
import com.sisol.sys_citas.repository.EspecialidadRepository;
import com.sisol.sys_citas.repository.MedicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/especialidad")
public class EspecialidadController {

    private static final Logger logger = LoggerFactory.getLogger(EspecialidadController.class);
    private final AgregarEspecialidadService agregarEspecialidadService;
    private final EspecialidadRepository especialidadRepository;
    private final MedicoRepository medicoRepository;

    public EspecialidadController(AgregarEspecialidadService agregarEspecialidadService,
                                 EspecialidadRepository especialidadRepository, MedicoRepository medicoRepository) {
        this.agregarEspecialidadService = agregarEspecialidadService;
        this.especialidadRepository = especialidadRepository;
        this.medicoRepository = medicoRepository;
    }

    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarEspecialidad(@Valid @ModelAttribute("agregarEspecialidadDTO") AgregarEspecialidadDTO agregarEspecialidadDTO,
                                                                 BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            fieldError -> fieldError.getField(),
                            fieldError -> fieldError.getDefaultMessage()
                    ));
            response.put("status", "error");
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            agregarEspecialidadService.agregarEspecialidad(agregarEspecialidadDTO);
            response.put("status", "success");
            response.put("message", "Especialidad agregada exitosamente.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Ocurrió un error inesperado durante el registro. Por favor, inténtalo de nuevo más tarde.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consultar")
    public String mostrarConsultarEspecialidades(Model model, HttpSession session) {
        logger.info("Accediendo a consulta de especialidades");
        
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
        
        logger.info("Usuario administrativo accediendo a consulta de especialidades: {}", usuarioSesion.getCorreo());
        
        // Agregar datos al modelo
        model.addAttribute("usuario", usuarioSesion);
        model.addAttribute("titulo", "Consultar Especialidades - SISOL");
        model.addAttribute("especialidades", especialidadRepository.findAll());
        
        return "pages/personal_administrativo/especialidades/reporte-especialidad :: reporte-especialidad";
    }
    
    // Endpoint REST para obtener todas las especialidades en formato JSON
    @GetMapping("/api/listar")
    @ResponseBody
    public ResponseEntity<List<com.sisol.sys_citas.model.Especialidad>> listarEspecialidades(HttpSession session) {
        logger.info("Obteniendo lista de especialidades");
        
        // Verificar que el usuario esté autenticado y tenga el rol correcto
        UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
        
        if (usuarioSesion == null || usuarioSesion.getRol() != Rol.ROLE_PERSONAL_ADMINISTRATIVO) {
            logger.warn("Usuario sin permisos para acceder a lista de especialidades");
            return ResponseEntity.status(403).build();
        }
        
        try {
            List<com.sisol.sys_citas.model.Especialidad> especialidades = especialidadRepository.findAll();
            logger.info("Encontradas {} especialidades", especialidades.size());
            

            
            return ResponseEntity.ok(especialidades);
        } catch (Exception e) {
            logger.error("Error al obtener lista de especialidades: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
    
    // Endpoint REST para obtener médicos por especialidad
    @GetMapping("/api/medicos-por-especialidad/{especialidadId}")
    @ResponseBody
    public ResponseEntity<List<Object[]>> getMedicosPorEspecialidad(@PathVariable Long especialidadId, HttpSession session) {
        logger.info("Obteniendo médicos para especialidad ID: {}", especialidadId);
        
        // Verificar que el usuario esté autenticado y tenga el rol correcto
        UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
        
        if (usuarioSesion == null || usuarioSesion.getRol() != Rol.ROLE_PERSONAL_ADMINISTRATIVO) {
            logger.warn("Usuario sin permisos para acceder a médicos por especialidad");
            return ResponseEntity.status(403).build();
        }
        
        try {
            // Obtener la especialidad para validar que existe
            var especialidad = especialidadRepository.findById(especialidadId);
            if (especialidad.isEmpty()) {
                logger.warn("Especialidad no encontrada con ID: {}", especialidadId);
                return ResponseEntity.notFound().build();
            }
            
            // Obtener todos los médicos del SP
            List<Object[]> todosMedicos = medicoRepository.listarPersonalMedico();
            
            // Filtrar por especialidad usando el nombre de la especialidad
            String nombreEspecialidad = especialidad.get().getNombre();
            List<Object[]> medicosFiltrados = todosMedicos.stream()
                .filter(medico -> {
                    String nombreEspecialidadMedico = (String) medico[3]; // especialidad está en la posición 3
                    return nombreEspecialidad.equals(nombreEspecialidadMedico);
                })
                .collect(Collectors.toList());
            
            logger.info("Encontrados {} médicos para la especialidad: {}", medicosFiltrados.size(), nombreEspecialidad);
            return ResponseEntity.ok(medicosFiltrados);
        } catch (Exception e) {
            logger.error("Error al obtener médicos por especialidad: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
} 