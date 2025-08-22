package com.sisol.sys_citas.service;

import com.sisol.sys_citas.dto.ReagendarCitaResponseDTO;
import com.sisol.sys_citas.model.Cita;
import com.sisol.sys_citas.model.Disponibilidad;
import com.sisol.sys_citas.model.Paciente;
import com.sisol.sys_citas.model.Usuario;
import com.sisol.sys_citas.repository.CitaRepository;
import com.sisol.sys_citas.repository.DisponibilidadRepository;
import com.sisol.sys_citas.repository.PacienteRepository;
import com.sisol.sys_citas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CitasService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private DisponibilidadRepository disponibilidadRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Transactional
    public void cancelarCita(Integer numTicket, String correoUsuario, String motivoCancelacion) {
        // Buscar el usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar el paciente asociado al usuario
        Paciente paciente = pacienteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Buscar la cita por número de ticket y paciente
        List<Cita> citasPaciente = citaRepository.findByPacienteIdWithRelations(paciente.getId());
        Cita cita = citasPaciente.stream()
                .filter(c -> c.getNumTicket().equals(numTicket))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cita no encontrada o no pertenece al usuario"));

        // Verificar que la cita esté en estado CONFIRMADA
        if (!"CONFIRMADA".equals(cita.getEstado())) {
            throw new RuntimeException("Solo se pueden cancelar citas confirmadas. Estado actual: " + cita.getEstado());
        }

        // Verificar que la cita no sea en el pasado
        if (cita.getDisponibilidad() != null) {
            LocalDateTime fechaHoraCita = LocalDateTime.of(
                    cita.getDisponibilidad().getFechaDisponibilidad(),
                    cita.getDisponibilidad().getHoraInicio()
            );
            if (fechaHoraCita.isBefore(LocalDateTime.now())) {
                throw new RuntimeException("No se puede cancelar una cita que ya pasó");
            }
        }

        // Cambiar el estado de la cita a CANCELADA
        cita.setEstado("CANCELADA");
        cita.setObservaciones(
                (cita.getObservaciones() != null ? cita.getObservaciones() + " | " : "") +
                "CANCELADA por paciente. " +
                (motivoCancelacion != null && !motivoCancelacion.trim().isEmpty() 
                    ? "Motivo: " + motivoCancelacion.trim() 
                    : "Sin motivo especificado")
        );
        cita.setFechaActualizacion(LocalDateTime.now());

        // Liberar la disponibilidad
        Disponibilidad disponibilidad = cita.getDisponibilidad();
        if (disponibilidad != null) {
            disponibilidad.setDisponible(true);
            disponibilidadRepository.save(disponibilidad);
        }

        // Guardar los cambios en la cita
        citaRepository.save(cita);
    }

    @Transactional
    public ReagendarCitaResponseDTO reagendarCita(String numTicket, String correoUsuario, Long nuevaDisponibilidadId) {
        // Buscar el usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar el paciente asociado al usuario
        Paciente paciente = pacienteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Buscar la cita por número de ticket y paciente
        List<Cita> citasPaciente = citaRepository.findByPacienteIdWithRelations(paciente.getId());
        Cita cita = citasPaciente.stream()
                .filter(c -> c.getNumTicket().toString().equals(numTicket))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cita no encontrada o no pertenece al usuario"));

        // Verificar que la cita esté en estado CONFIRMADA
        if (!"CONFIRMADA".equals(cita.getEstado())) {
            throw new RuntimeException("Solo se pueden reagendar citas confirmadas. Estado actual: " + cita.getEstado());
        }

        // Verificar que la cita no haya sido reagendada anteriormente
        if (cita.getObservaciones() != null && cita.getObservaciones().contains("REAGENDADA")) {
            throw new RuntimeException("Esta cita ya fue reagendada anteriormente. No se puede reagendar más de una vez.");
        }

        // Verificar que la cita no sea en el pasado
        if (cita.getDisponibilidad() != null) {
            LocalDateTime fechaHoraCita = LocalDateTime.of(
                    cita.getDisponibilidad().getFechaDisponibilidad(),
                    cita.getDisponibilidad().getHoraInicio()
            );
            if (fechaHoraCita.isBefore(LocalDateTime.now().plusHours(24))) {
                throw new RuntimeException("No se puede reagendar una cita con menos de 24 horas de anticipación");
            }
        }

        // Obtener la nueva disponibilidad
        Disponibilidad nuevaDisponibilidad = disponibilidadRepository.findById(nuevaDisponibilidadId)
                .orElseThrow(() -> new RuntimeException("La nueva disponibilidad no existe"));

        // Verificar que la nueva disponibilidad esté disponible
        if (!nuevaDisponibilidad.getDisponible()) {
            throw new RuntimeException("El horario seleccionado ya no está disponible");
        }

        // Verificar que la nueva disponibilidad sea del mismo médico
        if (!nuevaDisponibilidad.getMedico().getId().equals(cita.getMedico().getId())) {
            throw new RuntimeException("La nueva disponibilidad debe ser del mismo médico");
        }

        // Verificar que la nueva fecha no sea en el pasado
        LocalDateTime nuevaFechaHora = LocalDateTime.of(
                nuevaDisponibilidad.getFechaDisponibilidad(),
                nuevaDisponibilidad.getHoraInicio()
        );
        if (nuevaFechaHora.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede reagendar a una fecha y hora en el pasado");
        }

        // Liberar la disponibilidad anterior
        Disponibilidad disponibilidadAnterior = cita.getDisponibilidad();
        if (disponibilidadAnterior != null) {
            disponibilidadAnterior.setDisponible(true);
            disponibilidadRepository.save(disponibilidadAnterior);
        }

        // Ocupar la nueva disponibilidad
        nuevaDisponibilidad.setDisponible(false);
        disponibilidadRepository.save(nuevaDisponibilidad);

        // Actualizar la cita con la nueva disponibilidad
        cita.setDisponibilidad(nuevaDisponibilidad);
        cita.setObservaciones(
                (cita.getObservaciones() != null ? cita.getObservaciones() + " | " : "") +
                "REAGENDADA por paciente el " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
        cita.setFechaActualizacion(LocalDateTime.now());

        // Guardar los cambios en la cita
        citaRepository.save(cita);

        // Formatear la nueva fecha y hora para la respuesta
        String nuevaFechaFormateada = nuevaDisponibilidad.getFechaDisponibilidad()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String nuevaHoraFormateada = nuevaDisponibilidad.getHoraInicio()
                .format(DateTimeFormatter.ofPattern("HH:mm"));

        return new ReagendarCitaResponseDTO(
                "Cita reagendada exitosamente",
                true,
                nuevaFechaFormateada,
                nuevaHoraFormateada
        );
    }
}
