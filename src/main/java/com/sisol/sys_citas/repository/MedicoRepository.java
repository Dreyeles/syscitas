package com.sisol.sys_citas.repository;

import com.sisol.sys_citas.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    @Query("SELECT m FROM Medico m LEFT JOIN FETCH m.especialidad")
    List<Medico> findAllWithEspecialidad();
    // Buscar médico por nombres y apellidos
    Optional<Medico> findByNombresAndApellidos(String nombres, String apellidos);
    
    // Buscar médicos por especialidad
    @Query("SELECT m FROM Medico m WHERE m.especialidad.id = :especialidadId")
    List<Medico> findByEspecialidadId(@Param("especialidadId") Long especialidadId);
    
    // Verificar si existe un médico por DNI
    boolean existsByDni(String dni);
    
    // Verificar si existe un médico por número de colegiado
    boolean existsByNumeroColegiado(String numeroColegiado);
    
    // Verificar si existe un médico por email
    boolean existsByEmail(String email);
    
    // Método para usar el stored procedure sp_listar_personal_medico
    @Query(value = "CALL sp_listar_personal_medico()", nativeQuery = true)
    List<Object[]> listarPersonalMedico();
}
