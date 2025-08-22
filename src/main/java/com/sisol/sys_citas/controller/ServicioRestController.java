package com.sisol.sys_citas.controller;

import com.sisol.sys_citas.dto.ServicioListaDTO;
import com.sisol.sys_citas.dto.EspecialidadDTO;
import com.sisol.sys_citas.model.Servicio;
import com.sisol.sys_citas.repository.ServicioRepository;
import com.sisol.sys_citas.dto.UsuarioSesionDTO;
import com.sisol.sys_citas.enums.Rol;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/servicio/api")
@CrossOrigin(origins = "*")
public class ServicioRestController {

    private static final Logger logger = LoggerFactory.getLogger(ServicioRestController.class);

    @Autowired
    private ServicioRepository servicioRepository;

    // Obtener todos los servicios
    @GetMapping("/listar")
    public ResponseEntity<List<ServicioListaDTO>> listarServicios(HttpSession session) {
        logger.info("Obteniendo lista de servicios");
        
        try {
            // Verificar que el usuario esté autenticado y tenga el rol correcto
            UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
            
            if (usuarioSesion == null) {
                logger.warn("Usuario no autenticado");
                return ResponseEntity.status(401).build();
            }
            
            if (usuarioSesion.getRol() != Rol.ROLE_PERSONAL_ADMINISTRATIVO) {
                logger.warn("Usuario sin permisos para acceder a lista de servicios");
                return ResponseEntity.status(403).build();
            }
            
            List<Servicio> servicios = servicioRepository.findAllWithEspecialidad();
            logger.info("Encontrados {} servicios", servicios.size());
            
            List<ServicioListaDTO> serviciosDTO = servicios.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(serviciosDTO);
        } catch (Exception e) {
            logger.error("Error al obtener lista de servicios: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Obtener servicio por ID
    @GetMapping("/{id}")
    public ResponseEntity<ServicioListaDTO> obtenerServicioPorId(@PathVariable Long id, HttpSession session) {
        logger.info("Obteniendo servicio con ID: {}", id);
        
        try {
            // Verificar que el usuario esté autenticado y tenga el rol correcto
            UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
            
            if (usuarioSesion == null) {
                logger.warn("Usuario no autenticado");
                return ResponseEntity.status(401).build();
            }
            
            if (usuarioSesion.getRol() != Rol.ROLE_PERSONAL_ADMINISTRATIVO) {
                logger.warn("Usuario sin permisos para acceder a servicio");
                return ResponseEntity.status(403).build();
            }
            
            return servicioRepository.findByIdWithEspecialidad(id)
                    .map(this::convertirADTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error al obtener servicio con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Obtener servicios por especialidad
    @GetMapping("/especialidad/{especialidadId}")
    public ResponseEntity<List<ServicioListaDTO>> obtenerServiciosPorEspecialidad(@PathVariable Long especialidadId, HttpSession session) {
        logger.info("Obteniendo servicios para especialidad ID: {}", especialidadId);
        
        try {
            // Verificar que el usuario esté autenticado y tenga el rol correcto
            UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
            
            if (usuarioSesion == null) {
                logger.warn("Usuario no autenticado");
                return ResponseEntity.status(401).build();
            }
            
            if (usuarioSesion.getRol() != Rol.ROLE_PERSONAL_ADMINISTRATIVO) {
                logger.warn("Usuario sin permisos para acceder a servicios por especialidad");
                return ResponseEntity.status(403).build();
            }
            
            List<Servicio> servicios = servicioRepository.findByEspecialidadIdWithEspecialidad(especialidadId);
            logger.info("Encontrados {} servicios para especialidad ID {}", servicios.size(), especialidadId);
            
            List<ServicioListaDTO> serviciosDTO = servicios.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(serviciosDTO);
        } catch (Exception e) {
            logger.error("Error al obtener servicios para especialidad ID {}: {}", especialidadId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Método auxiliar para convertir Servicio a ServicioListaDTO
    private ServicioListaDTO convertirADTO(Servicio servicio) {
        EspecialidadDTO especialidadDTO = null;
        if (servicio.getEspecialidad() != null) {
            especialidadDTO = new EspecialidadDTO(
                servicio.getEspecialidad().getId(),
                servicio.getEspecialidad().getNombre()
            );
        }
        
        return new ServicioListaDTO(
            servicio.getId(),
            servicio.getNombre(),
            servicio.getDescripcion(),
            servicio.getPrecioServicio(),
            especialidadDTO
        );
    }
}
