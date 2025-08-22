package com.sisol.sys_citas.repository;

import com.sisol.sys_citas.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    // Métodos CRUD automáticos
    
    // Buscar servicio por nombre
    Optional<Servicio> findByNombre(String nombre);
    
    // Buscar servicios por especialidad
    @Query("SELECT s FROM Servicio s WHERE s.especialidad.id = :especialidadId")
    List<Servicio> findByEspecialidadId(@Param("especialidadId") Long especialidadId);
    
    // Buscar todos los servicios con especialidad cargada (JOIN FETCH)
    @Query("SELECT s FROM Servicio s LEFT JOIN FETCH s.especialidad")
    List<Servicio> findAllWithEspecialidad();
    
    // Buscar servicio por ID con especialidad cargada (JOIN FETCH)
    @Query("SELECT s FROM Servicio s LEFT JOIN FETCH s.especialidad WHERE s.id = :id")
    Optional<Servicio> findByIdWithEspecialidad(@Param("id") Long id);
    
    // Buscar servicios por especialidad con especialidad cargada (JOIN FETCH)
    @Query("SELECT s FROM Servicio s LEFT JOIN FETCH s.especialidad WHERE s.especialidad.id = :especialidadId")
    List<Servicio> findByEspecialidadIdWithEspecialidad(@Param("especialidadId") Long especialidadId);
}