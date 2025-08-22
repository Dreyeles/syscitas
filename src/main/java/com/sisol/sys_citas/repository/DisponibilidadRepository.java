package com.sisol.sys_citas.repository;

import com.sisol.sys_citas.model.Disponibilidad;
import com.sisol.sys_citas.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {
    
    // Buscar disponibilidad por médico, fecha, hora de inicio y estado disponible
    Optional<Disponibilidad> findByMedicoAndFechaDisponibilidadAndHoraInicioAndDisponible(
        Medico medico, LocalDate fechaDisponibilidad, LocalTime horaInicio, Boolean disponible);
    
    // Buscar fechas únicas disponibles por médico
    @Query("SELECT DISTINCT d.fechaDisponibilidad FROM Disponibilidad d WHERE d.medico.id = :medicoId AND d.disponible = true ORDER BY d.fechaDisponibilidad")
    List<LocalDate> findFechasDisponiblesByMedicoId(@Param("medicoId") Long medicoId);
    
    // Buscar horarios disponibles por fecha y médico
    @Query("SELECT d FROM Disponibilidad d WHERE d.medico.id = :medicoId AND d.fechaDisponibilidad = :fecha AND d.disponible = true ORDER BY d.horaInicio")
    List<Disponibilidad> findHorariosDisponiblesByMedicoIdAndFecha(@Param("medicoId") Long medicoId, @Param("fecha") LocalDate fecha);
} 