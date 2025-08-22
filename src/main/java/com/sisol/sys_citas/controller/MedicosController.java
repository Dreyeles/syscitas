package com.sisol.sys_citas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;
import com.sisol.sys_citas.dto.UsuarioSesionDTO;
import com.sisol.sys_citas.enums.Rol;
import com.sisol.sys_citas.repository.MedicoRepository;
import com.sisol.sys_citas.model.Medico;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Controller
@RequestMapping("/dashboard-administrativo/medicos")
public class MedicosController {
    
    private static final Logger logger = LoggerFactory.getLogger(MedicosController.class);
    private final MedicoRepository medicoRepository;

    public MedicosController(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }
    
    @GetMapping("/registrar")
    public String mostrarRegistroMedico(Model model, HttpSession session) {
        logger.info("Accediendo al registro de médicos");
        
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
        
        logger.info("Usuario administrativo accediendo a registro de médicos: {}", usuarioSesion.getCorreo());
        
        // Agregar datos al modelo
        model.addAttribute("usuario", usuarioSesion);
        model.addAttribute("titulo", "Registro de Médico - SISOL");
        
        return "pages/personal_administrativo/medicos/registro-medico :: registro-medico";
    }
    
    @GetMapping("/consultar")
    public String mostrarConsultarMedicos(Model model, HttpSession session) {
        logger.info("Accediendo a consulta de médicos");
        
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
        
        logger.info("Usuario administrativo accediendo a consulta de médicos: {}", usuarioSesion.getCorreo());
        
        // Agregar datos al modelo
        model.addAttribute("usuario", usuarioSesion);
        model.addAttribute("titulo", "Consultar Médicos - SISOL");
        model.addAttribute("medicos", medicoRepository.findAllWithEspecialidad());
        
        return "pages/personal_administrativo/medicos/consultar-medico :: consultar-medico";
    }
    
    // Endpoint REST para obtener todos los médicos en formato JSON
    @GetMapping("/api/listar")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> listarMedicos(HttpSession session) {
        logger.info("Obteniendo lista de médicos");
        
        try {
            // Verificar que el usuario esté autenticado y tenga el rol correcto
            UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
            
            if (usuarioSesion == null) {
                logger.warn("Usuario no autenticado");
                return ResponseEntity.status(401).build();
            }
            
            if (usuarioSesion.getRol() != Rol.ROLE_PERSONAL_ADMINISTRATIVO) {
                logger.warn("Usuario sin permisos para acceder a lista de médicos");
                return ResponseEntity.status(403).build();
            }
            
            // Obtener médicos de la base de datos
            List<Medico> medicos = medicoRepository.findAllWithEspecialidad();
            logger.info("Encontrados {} médicos", medicos.size());
            
            // Convertir a DTO simple para evitar problemas de serialización
            List<Map<String, Object>> medicosDTO = new ArrayList<>();
            
            for (Medico medico : medicos) {
                Map<String, Object> dto = new HashMap<>();
                dto.put("id", medico.getId());
                dto.put("nombres", medico.getNombres());
                dto.put("apellidos", medico.getApellidos());
                dto.put("dni", medico.getDni());
                dto.put("telefono", medico.getTelefono());
                dto.put("email", medico.getEmail());
                dto.put("numeroColegiado", medico.getNumeroColegiado());
                
                // Manejar enums de forma segura
                if (medico.getTurno() != null) {
                    dto.put("turno", medico.getTurno().getDisplayName());
                } else {
                    dto.put("turno", null);
                }
                
                if (medico.getEstado() != null) {
                    dto.put("estado", medico.getEstado().getDisplayName());
                } else {
                    dto.put("estado", null);
                }
                
                // Manejar especialidad de forma segura
                if (medico.getEspecialidad() != null) {
                    Map<String, Object> especialidadMap = new HashMap<>();
                    especialidadMap.put("id", medico.getEspecialidad().getId());
                    especialidadMap.put("nombre", medico.getEspecialidad().getNombre());
                    dto.put("especialidad", especialidadMap);
                } else {
                    dto.put("especialidad", null);
                }
                
                // Manejar fechas y horas de forma segura
                if (medico.getHoraInicioTurno() != null) {
                    dto.put("horaInicioTurno", medico.getHoraInicioTurno().toString());
                } else {
                    dto.put("horaInicioTurno", null);
                }
                
                if (medico.getHoraFinTurno() != null) {
                    dto.put("horaFinTurno", medico.getHoraFinTurno().toString());
                } else {
                    dto.put("horaFinTurno", null);
                }
                
                if (medico.getHoraInicioAtencion() != null) {
                    dto.put("horaInicioAtencion", medico.getHoraInicioAtencion().toString());
                } else {
                    dto.put("horaInicioAtencion", null);
                }
                
                if (medico.getHoraFinAtencion() != null) {
                    dto.put("horaFinAtencion", medico.getHoraFinAtencion().toString());
                } else {
                    dto.put("horaFinAtencion", null);
                }
                
                if (medico.getSexo() != null) {
                    dto.put("sexo", medico.getSexo().getDisplayName());
                } else {
                    dto.put("sexo", null);
                }
                
                if (medico.getEstadoCivil() != null) {
                    dto.put("estadoCivil", medico.getEstadoCivil().getDisplayName());
                } else {
                    dto.put("estadoCivil", null);
                }
                
                if (medico.getFechaNacimiento() != null) {
                    dto.put("fechaNacimiento", medico.getFechaNacimiento().toString());
                } else {
                    dto.put("fechaNacimiento", null);
                }
                
                medicosDTO.add(dto);
            }
            
            return ResponseEntity.ok(medicosDTO);
            
        } catch (Exception e) {
            logger.error("Error al obtener lista de médicos: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
} 