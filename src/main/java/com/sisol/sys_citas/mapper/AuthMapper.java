package com.sisol.sys_citas.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sisol.sys_citas.dto.UsuarioSesionDTO;
import com.sisol.sys_citas.model.Usuario; // Importar la entidad Usuario
import com.sisol.sys_citas.model.Paciente; // Importar la entidad Paciente

@Mapper
public interface AuthMapper {
    UsuarioSesionDTO verificarCorreo(@Param("correo") String correo);

    // --- Métodos de registro ---
    boolean existeCorreo(@Param("correo") String correo);
    boolean existeDni(@Param("dni") String dni);
    void insertarUsuario(Usuario usuario); // MyBatis mapeará los campos del objeto Usuario
    void insertarPaciente(Paciente paciente); // MyBatis mapeará los campos del objeto Paciente
    // --- Fin Métodos de registro ---

    // Métodos para obtener nombres y apellidos según el rol
    String obtenerNombrePaciente(@Param("id") Long id);
    String obtenerNombreMedico(@Param("id") Long id);
    String obtenerNombrePersonalAdministrativo(@Param("id") Long id);
}