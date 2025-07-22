package com.sisol.sys_citas.repository;

import com.sisol.sys_citas.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    // Métodos CRUD automáticos
}