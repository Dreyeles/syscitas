package com.sisol.sys_citas.repository;

import com.sisol.sys_citas.model.AtencionMedica;
import com.sisol.sys_citas.enums.EstadoAtencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AtencionMedicaRepository extends JpaRepository<AtencionMedica, Long> {

    // Buscar por cita
    Optional<AtencionMedica> findByCitaId(Long citaId);

    // Buscar por médico
    List<AtencionMedica> findByMedicoId(Long medicoId);

    // Buscar por estado
    List<AtencionMedica> findByEstado(EstadoAtencion estado);

    // Buscar por médico y estado
    List<AtencionMedica> findByMedicoIdAndEstado(Long medicoId, EstadoAtencion estado);

    // Buscar por fecha de atención
    List<AtencionMedica> findByFechaAtencionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Buscar por paciente (a través de la cita)
    @Query("SELECT am FROM AtencionMedica am WHERE am.cita.paciente.id = :pacienteId")
    List<AtencionMedica> findByPacienteId(@Param("pacienteId") Long pacienteId);

    // Buscar historial clínico de un paciente
    @Query("SELECT am FROM AtencionMedica am WHERE am.cita.paciente.id = :pacienteId AND am.estado = 'REALIZADO' ORDER BY am.fechaAtencion DESC")
    List<AtencionMedica> findHistorialClinicoByPacienteId(@Param("pacienteId") Long pacienteId);

    // Contar atenciones por médico en un período
    @Query("SELECT COUNT(am) FROM AtencionMedica am WHERE am.medico.id = :medicoId AND am.fechaAtencion BETWEEN :fechaInicio AND :fechaFin")
    Long countByMedicoAndPeriodo(@Param("medicoId") Long medicoId, 
                                 @Param("fechaInicio") LocalDateTime fechaInicio, 
                                 @Param("fechaFin") LocalDateTime fechaFin);
} 