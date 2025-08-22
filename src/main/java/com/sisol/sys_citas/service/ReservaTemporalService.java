package com.sisol.sys_citas.service;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@EnableScheduling
public class ReservaTemporalService {
    
    // Mapa para almacenar reservas temporales: disponibilidadId -> (usuarioId, fechaReserva)
    private final Map<Long, ReservaTemporal> reservasTemporales = new ConcurrentHashMap<>();
    
    // Tiempo de expiración de reserva en minutos
    private static final int TIEMPO_EXPIRACION_MINUTOS = 15;
    
    // Executor para limpiar reservas expiradas
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    public ReservaTemporalService() {
        // Programar limpieza automática cada 5 minutos
        scheduler.scheduleAtFixedRate(this::limpiarReservasExpiradas, 5, 5, TimeUnit.MINUTES);
    }
    
    /**
     * Intenta reservar temporalmente una disponibilidad
     * @param disponibilidadId ID de la disponibilidad
     * @param usuarioId ID del usuario que intenta reservar
     * @return true si la reserva fue exitosa, false si ya está reservada
     */
    public boolean reservarTemporalmente(Long disponibilidadId, Long usuarioId) {
        LocalDateTime ahora = LocalDateTime.now();
        
        // Verificar si ya existe una reserva
        ReservaTemporal reservaExistente = reservasTemporales.get(disponibilidadId);
        
        if (reservaExistente != null) {
            // Verificar si la reserva existente ha expirado
            if (reservaExistente.getFechaReserva().plusMinutes(TIEMPO_EXPIRACION_MINUTOS).isBefore(ahora)) {
                // La reserva expiró, podemos crear una nueva
                reservasTemporales.remove(disponibilidadId);
            } else {
                // La reserva aún es válida
                return false;
            }
        }
        
        // Crear nueva reserva temporal
        ReservaTemporal nuevaReserva = new ReservaTemporal(usuarioId, ahora);
        reservasTemporales.put(disponibilidadId, nuevaReserva);
        
        return true;
    }
    
    /**
     * Confirma una reserva temporal (convierte en cita real)
     * @param disponibilidadId ID de la disponibilidad
     * @param usuarioId ID del usuario que confirma
     * @return true si la confirmación fue exitosa
     */
    public boolean confirmarReserva(Long disponibilidadId, Long usuarioId) {
        ReservaTemporal reserva = reservasTemporales.get(disponibilidadId);
        
        if (reserva != null && reserva.getUsuarioId().equals(usuarioId)) {
            // Verificar que la reserva no haya expirado
            if (reserva.getFechaReserva().plusMinutes(TIEMPO_EXPIRACION_MINUTOS).isAfter(LocalDateTime.now())) {
                // Remover la reserva temporal
                reservasTemporales.remove(disponibilidadId);
                return true;
            } else {
                // La reserva expiró
                reservasTemporales.remove(disponibilidadId);
                return false;
            }
        }
        
        return false;
    }
    
    /**
     * Libera una reserva temporal
     * @param disponibilidadId ID de la disponibilidad
     * @param usuarioId ID del usuario que libera
     * @return true si la liberación fue exitosa
     */
    public boolean liberarReserva(Long disponibilidadId, Long usuarioId) {
        ReservaTemporal reserva = reservasTemporales.get(disponibilidadId);
        
        if (reserva != null && reserva.getUsuarioId().equals(usuarioId)) {
            reservasTemporales.remove(disponibilidadId);
            return true;
        }
        
        return false;
    }
    
    /**
     * Verifica si una disponibilidad está reservada temporalmente
     * @param disponibilidadId ID de la disponibilidad
     * @return true si está reservada temporalmente
     */
    public boolean estaReservadaTemporalmente(Long disponibilidadId) {
        ReservaTemporal reserva = reservasTemporales.get(disponibilidadId);
        
        if (reserva != null) {
            // Verificar si la reserva ha expirado
            if (reserva.getFechaReserva().plusMinutes(TIEMPO_EXPIRACION_MINUTOS).isBefore(LocalDateTime.now())) {
                // La reserva expiró, removerla
                reservasTemporales.remove(disponibilidadId);
                return false;
            }
            return true;
        }
        
        return false;
    }
    
    /**
     * Verifica si una disponibilidad está reservada por un usuario específico
     * @param disponibilidadId ID de la disponibilidad
     * @param usuarioId ID del usuario
     * @return true si está reservada por el usuario
     */
    public boolean estaReservadaPorUsuario(Long disponibilidadId, Long usuarioId) {
        ReservaTemporal reserva = reservasTemporales.get(disponibilidadId);
        
        if (reserva != null && reserva.getUsuarioId().equals(usuarioId)) {
            // Verificar si la reserva ha expirado
            if (reserva.getFechaReserva().plusMinutes(TIEMPO_EXPIRACION_MINUTOS).isBefore(LocalDateTime.now())) {
                // La reserva expiró, removerla
                reservasTemporales.remove(disponibilidadId);
                return false;
            }
            return true;
        }
        
        return false;
    }
    
    /**
     * Limpia todas las reservas expiradas
     */
    @Scheduled(fixedRate = 300000) // Cada 5 minutos
    public void limpiarReservasExpiradas() {
        LocalDateTime ahora = LocalDateTime.now();
        
        reservasTemporales.entrySet().removeIf(entry -> {
            ReservaTemporal reserva = entry.getValue();
            return reserva.getFechaReserva().plusMinutes(TIEMPO_EXPIRACION_MINUTOS).isBefore(ahora);
        });
    }
    
    /**
     * Clase interna para representar una reserva temporal
     */
    private static class ReservaTemporal {
        private final Long usuarioId;
        private final LocalDateTime fechaReserva;
        
        public ReservaTemporal(Long usuarioId, LocalDateTime fechaReserva) {
            this.usuarioId = usuarioId;
            this.fechaReserva = fechaReserva;
        }
        
        public Long getUsuarioId() {
            return usuarioId;
        }
        
        public LocalDateTime getFechaReserva() {
            return fechaReserva;
        }
    }
}
