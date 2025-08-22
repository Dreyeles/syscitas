package com.sisol.sys_citas.repository;

import com.sisol.sys_citas.model.Paciente;
import com.sisol.sys_citas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importa Optional

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByDni(String dni);
    boolean existsByDni(String dni); // Este es muy útil para la validación
    Optional<Paciente> findByUsuario(Usuario usuario);
}