package com.sisol.sys_citas.repository;

import com.sisol.sys_citas.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    // Puedes añadir métodos personalizados aquí, ej:
    // Optional<Especialidad> findByNombre(String nombre);
}