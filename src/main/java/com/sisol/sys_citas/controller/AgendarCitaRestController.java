package com.sisol.sys_citas.controller;

import com.sisol.sys_citas.dto.*;
import com.sisol.sys_citas.model.Paciente;
import com.sisol.sys_citas.model.Usuario;
import com.sisol.sys_citas.repository.PacienteRepository;
import com.sisol.sys_citas.repository.UsuarioRepository;
import com.sisol.sys_citas.service.AgendarCitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/agendar-cita")
public class AgendarCitaRestController {

    @Autowired
    private AgendarCitaService agendarCitaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // Obtener especialidades
    @GetMapping("/especialidades")
    public ResponseEntity<List<EspecialidadDTO>> obtenerEspecialidades() {
        List<EspecialidadDTO> especialidades = agendarCitaService.obtenerEspecialidades();
        return ResponseEntity.ok(especialidades);
    }

    // Obtener servicios por especialidad
    @GetMapping("/servicios/{especialidadId}")
    public ResponseEntity<List<ServicioDTO>> obtenerServiciosPorEspecialidad(@PathVariable Long especialidadId) {
        List<ServicioDTO> servicios = agendarCitaService.obtenerServiciosPorEspecialidad(especialidadId);
        return ResponseEntity.ok(servicios);
    }

    // Obtener médicos por especialidad
    @GetMapping("/medicos/{especialidadId}")
    public ResponseEntity<List<MedicoDTO>> obtenerMedicosPorEspecialidad(@PathVariable Long especialidadId) {
        List<MedicoDTO> medicos = agendarCitaService.obtenerMedicosPorEspecialidad(especialidadId);
        return ResponseEntity.ok(medicos);
    }

    // Obtener médicos por especialidad y turno
    @GetMapping("/medicos/{especialidadId}/{turno}")
    public ResponseEntity<List<MedicoDTO>> obtenerMedicosPorEspecialidadYTurno(
            @PathVariable Long especialidadId, 
            @PathVariable String turno) {
        List<MedicoDTO> medicos = agendarCitaService.obtenerMedicosPorEspecialidadYTurno(especialidadId, turno);
        return ResponseEntity.ok(medicos);
    }

    // Obtener fechas disponibles por médico
    @GetMapping("/fechas/{medicoId}")
    public ResponseEntity<List<LocalDate>> obtenerFechasDisponiblesPorMedico(@PathVariable Long medicoId) {
        List<LocalDate> fechas = agendarCitaService.obtenerFechasDisponiblesPorMedico(medicoId);
        return ResponseEntity.ok(fechas);
    }

    // Obtener horarios disponibles por fecha y médico
    @GetMapping("/horarios/{medicoId}/{fecha}")
    public ResponseEntity<List<DisponibilidadDTO>> obtenerHorariosDisponiblesPorFecha(
            @PathVariable Long medicoId, 
            @PathVariable String fecha) {
        LocalDate fechaLocal = LocalDate.parse(fecha);
        List<DisponibilidadDTO> horarios = agendarCitaService.obtenerHorariosDisponiblesPorFecha(medicoId, fechaLocal);
        return ResponseEntity.ok(horarios);
    }

    // Agendar cita
    @PostMapping("/agendar")
    public ResponseEntity<AgendarCitaResponseDTO> agendarCita(@RequestBody AgendarCitaDTO agendarCitaDTO) {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String correo = authentication.getName();
            
            // Buscar el usuario en la base de datos
            Usuario usuario = usuarioRepository.findByCorreo(correo)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Buscar el paciente asociado al usuario
            Paciente paciente = pacienteRepository.findByUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            
            agendarCitaService.agendarCita(agendarCitaDTO, paciente.getId());
            
            // Crear respuesta con información del paciente
            AgendarCitaResponseDTO response = new AgendarCitaResponseDTO(
                "Cita agendada exitosamente",
                paciente.getNombres(),
                paciente.getApellidos()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            AgendarCitaResponseDTO errorResponse = new AgendarCitaResponseDTO(
                "Error al agendar la cita: " + e.getMessage(),
                null,
                null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Validar disponibilidad antes de agendar
    @GetMapping("/validar-disponibilidad/{disponibilidadId}")
    public ResponseEntity<Map<String, Object>> validarDisponibilidad(@PathVariable Long disponibilidadId) {
        try {
            boolean disponible = agendarCitaService.validarDisponibilidad(disponibilidadId);
            Map<String, Object> response = new HashMap<>();
            response.put("disponible", disponible);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("disponible", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Reservar temporalmente una disponibilidad
    @PostMapping("/reservar-temporalmente/{disponibilidadId}")
    public ResponseEntity<Map<String, Object>> reservarTemporalmente(@PathVariable Long disponibilidadId) {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String correo = authentication.getName();
            
            // Buscar el usuario en la base de datos
            Usuario usuario = usuarioRepository.findByCorreo(correo)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Buscar el paciente asociado al usuario
            Paciente paciente = pacienteRepository.findByUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            
            // Intentar reservar temporalmente
            boolean reservado = agendarCitaService.reservarTemporalmente(disponibilidadId, paciente.getId());
            
            Map<String, Object> response = new HashMap<>();
            if (reservado) {
                response.put("reservado", true);
                response.put("mensaje", "Disponibilidad reservada temporalmente por 15 minutos");
            } else {
                response.put("reservado", false);
                response.put("mensaje", "La disponibilidad no está disponible o ya está reservada");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("reservado", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Liberar reserva temporal
    @PostMapping("/liberar-reserva/{disponibilidadId}")
    public ResponseEntity<Map<String, Object>> liberarReserva(@PathVariable Long disponibilidadId) {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String correo = authentication.getName();
            
            // Buscar el usuario en la base de datos
            Usuario usuario = usuarioRepository.findByCorreo(correo)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Buscar el paciente asociado al usuario
            Paciente paciente = pacienteRepository.findByUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            
            // Liberar la reserva temporal
            boolean liberado = agendarCitaService.liberarReservaTemporal(disponibilidadId, paciente.getId());
            
            Map<String, Object> response = new HashMap<>();
            response.put("liberado", liberado);
            response.put("mensaje", liberado ? "Reserva liberada exitosamente" : "No se encontró reserva para liberar");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("liberado", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 