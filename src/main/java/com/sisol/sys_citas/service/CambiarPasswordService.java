package com.sisol.sys_citas.service;

import com.sisol.sys_citas.dto.CambiarPasswordDTO;
import com.sisol.sys_citas.model.Usuario;
import com.sisol.sys_citas.repository.UsuarioRepository;
import com.sisol.sys_citas.exceptions.AutenticacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CambiarPasswordService {

    private static final Logger logger = LoggerFactory.getLogger(CambiarPasswordService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void cambiarPassword(Long usuarioId, CambiarPasswordDTO cambiarPasswordDTO) {
        logger.info("Iniciando cambio de contraseña para usuario ID: {}", usuarioId);

        // Validar que las contraseñas coincidan
        if (!cambiarPasswordDTO.passwordsCoinciden()) {
            logger.warn("Las contraseñas no coinciden para usuario ID: {}", usuarioId);
            throw new AutenticacionException("Las contraseñas no coinciden");
        }

        // Buscar el usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new AutenticacionException("Usuario no encontrado"));

        // Verificar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(cambiarPasswordDTO.getPasswordActual(), usuario.getContrasenia())) {
            logger.warn("Contraseña actual incorrecta para usuario ID: {}", usuarioId);
            throw new AutenticacionException("La contraseña actual es incorrecta");
        }

        // Verificar que la nueva contraseña sea diferente a la actual
        if (passwordEncoder.matches(cambiarPasswordDTO.getPasswordNueva(), usuario.getContrasenia())) {
            logger.warn("La nueva contraseña es igual a la actual para usuario ID: {}", usuarioId);
            throw new AutenticacionException("La nueva contraseña debe ser diferente a la actual");
        }

        // Encriptar y guardar la nueva contraseña
        String nuevaPasswordEncriptada = passwordEncoder.encode(cambiarPasswordDTO.getPasswordNueva());
        usuario.setContrasenia(nuevaPasswordEncriptada);
        
        // La fecha de actualización se actualiza automáticamente con @PreUpdate
        usuarioRepository.save(usuario);

        logger.info("Contraseña cambiada exitosamente para usuario ID: {}", usuarioId);
    }
} 