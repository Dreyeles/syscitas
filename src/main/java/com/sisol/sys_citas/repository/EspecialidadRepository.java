package com.sisol.sys_citas.repository;

import com.sisol.sys_citas.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    // Método para verificar si existe una especialidad por nombre
    boolean existsByNombre(String nombre);
    
    // Buscar especialidad por nombre
    Optional<Especialidad> findByNombre(String nombre);
    
    // Buscar especialidades por nombre (LIKE)
    @Query("SELECT e FROM Especialidad e WHERE e.nombre LIKE %:nombre%")
    List<Especialidad> findByNombreContaining(@Param("nombre") String nombre);
    
    // Consulta con filtros dinámicos
    @Query("SELECT e FROM Especialidad e WHERE " +
           "(:nombre IS NULL OR e.nombre LIKE %:nombre%) AND " +
           "(:descripcion IS NULL OR e.descripcion LIKE %:descripcion%)")
    List<Especialidad> findByFiltros(@Param("nombre") String nombre, 
                                    @Param("descripcion") String descripcion);
}