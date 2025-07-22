package com.sisol.sys_citas.repository;

import com.sisol.sys_citas.model.Usuario; // Asegúrate de importar tu clase Usuario
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importa Optional para métodos que pueden no encontrar un resultado

@Repository // Anotación que marca esta interfaz como un componente de repositorio de Spring
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método para buscar un usuario por su correo electrónico.
    // Spring Data JPA generará automáticamente la consulta SQL a partir del nombre del método.
    Optional<Usuario> findByCorreo(String correo);

    // Método para verificar si ya existe un usuario con un correo electrónico dado.
    // Muy útil para la validación durante el registro.
    boolean existsByCorreo(String correo);
}