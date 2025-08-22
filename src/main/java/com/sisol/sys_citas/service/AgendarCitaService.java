package com.sisol.sys_citas.service;

import com.sisol.sys_citas.dto.*;
import com.sisol.sys_citas.model.*;
import com.sisol.sys_citas.repository.*;
import com.sisol.sys_citas.enums.EstadoPago;
import com.sisol.sys_citas.enums.Comprobante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class AgendarCitaService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private DisponibilidadRepository disponibilidadRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ReservaTemporalService reservaTemporalService;

    // Obtener todas las especialidades
    public List<EspecialidadDTO> obtenerEspecialidades() {
        return especialidadRepository.findAll().stream()
                .map(especialidad -> new EspecialidadDTO(especialidad.getId(), especialidad.getNombre()))
                .collect(Collectors.toList());
    }

    // Obtener servicios por especialidad
    public List<ServicioDTO> obtenerServiciosPorEspecialidad(Long especialidadId) {
        return servicioRepository.findByEspecialidadId(especialidadId).stream()
                .map(servicio -> new ServicioDTO(
                        servicio.getId(),
                        servicio.getNombre(),
                        servicio.getPrecioServicio(),
                        servicio.getEspecialidad().getId()))
                .collect(Collectors.toList());
    }

    // Obtener médicos por especialidad
    public List<MedicoDTO> obtenerMedicosPorEspecialidad(Long especialidadId) {
        return medicoRepository.findByEspecialidadId(especialidadId).stream()
                .map(medico -> new MedicoDTO(
                        medico.getId(),
                        medico.getNombres(),
                        medico.getApellidos(),
                        medico.getEspecialidad().getId(),
                        medico.getTurno() != null ? medico.getTurno().getDisplayName() : null))
                .collect(Collectors.toList());
    }

    // Obtener médicos por especialidad y turno
    public List<MedicoDTO> obtenerMedicosPorEspecialidadYTurno(Long especialidadId, String turno) {
        return medicoRepository.findByEspecialidadId(especialidadId).stream()
                .filter(medico -> {
                    if (turno == null || turno.isEmpty() || "TODOS".equals(turno)) {
                        return true; // Mostrar todos los médicos
                    }
                    return medico.getTurno() != null && medico.getTurno().getDisplayName().equals(turno);
                })
                .map(medico -> new MedicoDTO(
                        medico.getId(),
                        medico.getNombres(),
                        medico.getApellidos(),
                        medico.getEspecialidad().getId(),
                        medico.getTurno() != null ? medico.getTurno().getDisplayName() : null))
                .collect(Collectors.toList());
    }

    // Obtener fechas disponibles por médico
    public List<LocalDate> obtenerFechasDisponiblesPorMedico(Long medicoId) {
        return disponibilidadRepository.findFechasDisponiblesByMedicoId(medicoId);
    }

    // Obtener horarios disponibles por fecha y médico
    public List<DisponibilidadDTO> obtenerHorariosDisponiblesPorFecha(Long medicoId, LocalDate fecha) {
        return disponibilidadRepository.findHorariosDisponiblesByMedicoIdAndFecha(medicoId, fecha).stream()
                .filter(disponibilidad -> {
                    // Verificar si está disponible en la base de datos
                    if (!disponibilidad.getDisponible()) {
                        return false;
                    }
                    
                    // Verificar si no está reservada temporalmente
                    return !reservaTemporalService.estaReservadaTemporalmente(disponibilidad.getId());
                })
                .map(disponibilidad -> new DisponibilidadDTO(
                        disponibilidad.getId(),
                        disponibilidad.getMedico().getId(),
                        disponibilidad.getFechaDisponibilidad(),
                        disponibilidad.getHoraInicio(),
                        disponibilidad.getHoraFin(),
                        disponibilidad.getDisponible()))
                .collect(Collectors.toList());
    }

    // Agendar cita y crear pago
    @Transactional
    public void agendarCita(AgendarCitaDTO agendarCitaDTO, Long pacienteId) {
        // Obtener las entidades necesarias
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        
        Medico medico = medicoRepository.findById(agendarCitaDTO.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        
        Servicio servicio = servicioRepository.findById(agendarCitaDTO.getServicioId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        
        Disponibilidad disponibilidad = disponibilidadRepository.findById(agendarCitaDTO.getDisponibilidadId())
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));

        // Verificar que la disponibilidad esté disponible en la base de datos
        if (!disponibilidad.getDisponible()) {
            throw new RuntimeException("La disponibilidad seleccionada no está disponible");
        }

        // Verificar que el paciente tenga una reserva temporal válida para esta disponibilidad - COMENTADO PARA DEBUG
        /*
        if (!reservaTemporalService.estaReservadaPorUsuario(disponibilidad.getId(), pacienteId)) {
            throw new RuntimeException("No tiene una reserva temporal válida para esta disponibilidad. Por favor, seleccione el horario nuevamente.");
        }

        // Confirmar la reserva temporal (removerla del sistema de reservas)
        if (!reservaTemporalService.confirmarReserva(disponibilidad.getId(), pacienteId)) {
            throw new RuntimeException("La reserva temporal ha expirado. Por favor, seleccione otro horario.");
        }
        */

        // Crear el pago primero (para debug)
        Pago pago = new Pago();
        pago.setMonto(agendarCitaDTO.getPrecio());
        pago.setFechaPago(LocalDateTime.now());
        pago.setCodigoTransaccion(generarCodigoTransaccion());
        pago.setEstado(EstadoPago.CONFIRMADO);
        pago.setComprobante(Comprobante.BOLETA_VENTA);
        // No establecer la cita aquí, se hará después

        // Guardar el pago primero
        Pago pagoGuardado = pagoRepository.save(pago);

        // Crear la cita con referencia al pago
        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setServicio(servicio);
        cita.setEstado("CONFIRMADA");
        cita.setObservaciones("Cita agendada automáticamente");
        cita.setDisponibilidad(disponibilidad);
        cita.setNumTicket(disponibilidad.getNumTicket());
        cita.setPago(pagoGuardado); // Establecer la referencia al pago

        // Guardar la cita
        Cita citaGuardada = citaRepository.save(cita);

        // Actualizar el pago con la referencia a la cita
        pagoGuardado.setCita(citaGuardada);
        pagoRepository.save(pagoGuardado);

        // Marcar la disponibilidad como no disponible
        disponibilidad.setDisponible(false);
        disponibilidadRepository.save(disponibilidad);
    }

    // Validar disponibilidad antes de agendar
    public boolean validarDisponibilidad(Long disponibilidadId) {
        Optional<Disponibilidad> disponibilidadOpt = disponibilidadRepository.findById(disponibilidadId);
        if (!disponibilidadOpt.isPresent()) {
            return false;
        }
        
        Disponibilidad disponibilidad = disponibilidadOpt.get();
        boolean disponibleEnBD = disponibilidad.getDisponible() && 
               disponibilidad.getFechaDisponibilidad().isAfter(LocalDate.now().minusDays(1));
        
        // Verificar que no esté reservada temporalmente
        boolean noReservadaTemporalmente = !reservaTemporalService.estaReservadaTemporalmente(disponibilidadId);
        
        return disponibleEnBD && noReservadaTemporalmente;
    }

    // Reservar temporalmente una disponibilidad
    public boolean reservarTemporalmente(Long disponibilidadId, Long pacienteId) {
        // Verificar que la disponibilidad esté disponible
        if (!validarDisponibilidad(disponibilidadId)) {
            return false;
        }
        
        // Intentar reservar temporalmente
        return reservaTemporalService.reservarTemporalmente(disponibilidadId, pacienteId);
    }

    // Liberar reserva temporal
    public boolean liberarReservaTemporal(Long disponibilidadId, Long pacienteId) {
        return reservaTemporalService.liberarReserva(disponibilidadId, pacienteId);
    }

    // Generar código de transacción único
    private Long generarCodigoTransaccion() {
        return System.currentTimeMillis() + (long)(Math.random() * 1000);
    }
} 