package com.sisol.sys_citas.service;

import com.sisol.sys_citas.dto.RegistroUsuarioDTO;
import com.sisol.sys_citas.enums.Rol;
import com.sisol.sys_citas.exceptions.RegistroException; // Usar la excepción personalizada
import com.sisol.sys_citas.model.Paciente;
import com.sisol.sys_citas.model.Usuario;
import com.sisol.sys_citas.repository.PacienteRepository;
import com.sisol.sys_citas.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistroService {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistroService(UsuarioRepository usuarioRepository, PacienteRepository pacienteRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Paciente registrarNuevoPaciente(RegistroUsuarioDTO registroDTO) {
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

        // 4. Crear y guardar la entidad Usuario
        Usuario usuario = new Usuario();
        usuario.setCorreo(registroDTO.getCorreo());
        usuario.setContrasenia(passwordEncoder.encode(registroDTO.getContrasenia()));
        usuario.setRol(Rol.ROLE_PACIENTE);
        usuario.setActivo(1); // Setear a 1 o true según cómo lo maneje tu entidad/DB

        // Guarda el usuario para que JPA le asigne un ID (si es autoincremental)
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // 5. Crear y guardar la entidad Paciente, vinculándola al Usuario
        Paciente paciente = new Paciente();
        // IMPORTANTE: Si la relación es @OneToOne con @MapsId, el ID del paciente será el mismo que el del usuario.
        paciente.setId(usuarioGuardado.getId());
        paciente.setUsuario(usuarioGuardado); // Vincula la entidad Usuario al Paciente

        paciente.setNombres(registroDTO.getNombres());
        paciente.setApellidos(registroDTO.getApellidos());
        paciente.setDni(registroDTO.getDni());
        paciente.setFechaNacimiento(registroDTO.getFechaNacimiento());
        paciente.setSexo(registroDTO.getSexo());
        paciente.setEstadoCivil(registroDTO.getEstadoCivil());
        paciente.setDireccion(registroDTO.getDireccion());
        paciente.setTelefono(registroDTO.getTelefono());
        paciente.setAlergias(registroDTO.getAlergias());
        paciente.setGrupoSanguineo(registroDTO.getGrupoSanguineo());
        // --- Asignar los nuevos campos de contacto de emergencia ---
        paciente.setContactoEmergenciaNombre(registroDTO.getContactoEmergenciaNombre());
        paciente.setContactoEmergenciaTelefono(registroDTO.getContactoEmergenciaTelefono());
        // --- Fin asignación de nuevos campos ---

        return pacienteRepository.save(paciente);
    }
}