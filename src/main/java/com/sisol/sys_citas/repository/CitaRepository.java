package com.sisol.sys_citas.repository;

import com.sisol.sys_citas.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    // Métodos CRUD automáticos
}