package com.sisol.sys_citas.repository;

import com.sisol.sys_citas.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    // Métodos CRUD automáticos
    
    // Buscar citas por ID de paciente
    List<Cita> findByPacienteId(Long pacienteId);
    
    // Buscar citas por ID de paciente con todas las relaciones cargadas
    @Query("SELECT c FROM Cita c " +
           "LEFT JOIN FETCH c.paciente p " +
           "LEFT JOIN FETCH c.disponibilidad d " +
           "LEFT JOIN FETCH c.servicio s " +
           "LEFT JOIN FETCH c.medico m " +
           "LEFT JOIN FETCH m.especialidad e " +
           "LEFT JOIN FETCH c.pago " +
           "WHERE p.id = :pacienteId")
    List<Cita> findByPacienteIdWithRelations(@Param("pacienteId") Long pacienteId);
}