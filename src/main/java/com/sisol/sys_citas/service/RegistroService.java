package com.sisol.sys_citas.service;

import com.sisol.sys_citas.dto.RegistroUsuarioDTO;
import com.sisol.sys_citas.enums.Rol;
import com.sisol.sys_citas.clients.reciec.exception.RegistroException;
import com.sisol.sys_citas.model.Paciente;
import com.sisol.sys_citas.model.Usuario;
import com.sisol.sys_citas.repository.PacienteRepository;
import com.sisol.sys_citas.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistroService {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder; // Aunque se inyecta, no se usará para encriptar la contraseña en este método.

    public RegistroService(UsuarioRepository usuarioRepository, PacienteRepository pacienteRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(rollbackFor = Exception.class)
    public Paciente registrarNuevoPaciente(RegistroUsuarioDTO registroDTO) {
        try {
            System.out.println("=== INICIANDO REGISTRO DE PACIENTE ===");
            
            // 1. Validar que las contraseñas coincidan
            if (!registroDTO.contraseniasCoinciden()) {
                throw new RegistroException("Las contraseñas no coinciden.");
            }

            // 2. Validar que el correo no esté ya registrado
            if (usuarioRepository.existsByCorreo(registroDTO.getCorreo())) {
                throw new RegistroException("El correo '" + registroDTO.getCorreo() + "' ya está registrado.");
            }

            // 3. Validar que el DNI no esté ya registrado
            if (pacienteRepository.existsByDni(registroDTO.getDni())) {
                throw new RegistroException("El DNI '" + registroDTO.getDni() + "' ya está registrado.");
            }

            System.out.println("=== VALIDACIONES PASADAS, CREANDO USUARIO ===");

            // 4. Crear y guardar la entidad Usuario
            Usuario usuario = new Usuario();
            usuario.setCorreo(registroDTO.getCorreo());
            usuario.setContrasenia(registroDTO.getContrasenia());
            usuario.setRol(Rol.ROLE_PACIENTE);
            usuario.setActivo(1);
            usuario.setFechaRegistro(LocalDateTime.now());
            usuario.setFechaActualizacion(LocalDateTime.now());

            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            System.out.println("Usuario guardado con ID: " + usuarioGuardado.getId());

            System.out.println("=== CREANDO PACIENTE ===");

            // 5. Crear la entidad Paciente usando el constructor correcto
            Paciente paciente = new Paciente(
                registroDTO.getDni(),
                registroDTO.getNombres(),
                registroDTO.getApellidos(),
                registroDTO.getTelefono(),
                registroDTO.getEstadoCivil(),
                registroDTO.getDireccion(),
                registroDTO.getDistrito(),
                registroDTO.getContactoEmergenciaNombre(),
                registroDTO.getContactoEmergenciaTelefono(),
                registroDTO.getContactoEmergenciaParentesco(),
                registroDTO.getFechaNacimiento(),
                registroDTO.getSexo(),
                usuarioGuardado
            );

            Paciente pacienteGuardado = pacienteRepository.save(paciente);
            System.out.println("Paciente guardado con ID: " + pacienteGuardado.getId());
            System.out.println("=== REGISTRO EXITOSO ===");

            return pacienteGuardado;
        } catch (RegistroException e) {
            System.out.println("ERROR DE REGISTRO: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("ERROR INESPERADO: " + e.getMessage());
            e.printStackTrace();
            throw new RegistroException("Error al registrar el paciente: " + e.getMessage());
        }
    }
}