package com.sisol.sys_citas.repository;

import com.sisol.sys_citas.model.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {

    // Buscar por paciente
    Optional<HistoriaClinica> findByPacienteId(Long pacienteId);

    // Verificar si existe historia clínica para un paciente
    boolean existsByPacienteId(Long pacienteId);
} 