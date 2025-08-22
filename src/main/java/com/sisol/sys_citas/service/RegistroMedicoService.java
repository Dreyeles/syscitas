package com.sisol.sys_citas.service;

import com.sisol.sys_citas.dto.RegistroMedicoDTO;
import com.sisol.sys_citas.model.Medico;
import com.sisol.sys_citas.model.Especialidad;
import com.sisol.sys_citas.model.Usuario;
import com.sisol.sys_citas.repository.MedicoRepository;
import com.sisol.sys_citas.repository.EspecialidadRepository;
import com.sisol.sys_citas.repository.UsuarioRepository;
import com.sisol.sys_citas.enums.Rol;
import com.sisol.sys_citas.clients.reciec.exception.RegistroException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class RegistroMedicoService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistroMedicoService(MedicoRepository medicoRepository, 
                                EspecialidadRepository especialidadRepository,
                                UsuarioRepository usuarioRepository,
                                PasswordEncoder passwordEncoder) {
        this.medicoRepository = medicoRepository;
        this.especialidadRepository = especialidadRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Medico registrarMedico(RegistroMedicoDTO medicoDTO) {
        // Validar que el DNI no esté ya registrado
        if (medicoRepository.existsByDni(medicoDTO.getDni())) {
            throw new RegistroException("El DNI '" + medicoDTO.getDni() + "' ya está registrado.");
        }

        // Validar que el número de colegiado no esté ya registrado
        if (medicoRepository.existsByNumeroColegiado(medicoDTO.getNumeroColegiado())) {
            throw new RegistroException("El número de colegiado '" + medicoDTO.getNumeroColegiado() + "' ya está registrado.");
        }

        // Validar que el email no esté ya registrado (si se proporciona)
        if (medicoDTO.getEmail() != null && !medicoDTO.getEmail().trim().isEmpty()) {
            if (medicoRepository.existsByEmail(medicoDTO.getEmail())) {
                throw new RegistroException("El email '" + medicoDTO.getEmail() + "' ya está registrado.");
            }
        }

        // Validar que el email no esté ya registrado como usuario
        if (usuarioRepository.existsByCorreo(medicoDTO.getEmail())) {
            throw new RegistroException("El email '" + medicoDTO.getEmail() + "' ya está registrado como usuario.");
        }

        // Validar que las contraseñas coincidan
        if (!medicoDTO.getPassword().equals(medicoDTO.getConfirmPassword())) {
            throw new RegistroException("Las contraseñas no coinciden.");
        }

        // Obtener la especialidad
        Especialidad especialidad = especialidadRepository.findById(medicoDTO.getEspecialidadId())
                .orElseThrow(() -> new RegistroException("La especialidad seleccionada no existe."));

        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setCorreo(medicoDTO.getEmail());
        usuario.setContrasenia(passwordEncoder.encode(medicoDTO.getPassword()));
        usuario.setRol(Rol.ROLE_MEDICO);
        usuario.setActivo(1);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setFechaActualizacion(LocalDateTime.now());

        // Guardar el usuario primero
        usuario = usuarioRepository.save(usuario);

        // Crear el médico
        Medico medico = new Medico();
        medico.setNombres(medicoDTO.getNombres());
        medico.setApellidos(medicoDTO.getApellidos());
        medico.setNumeroColegiado(medicoDTO.getNumeroColegiado());
        medico.setTurno(Medico.TurnoMedico.valueOf(medicoDTO.getTurno()));
        // Fallback de horas si no vinieron desde el front (seguridad adicional)
        if (medicoDTO.getHoraInicioTurno() == null || medicoDTO.getHoraFinTurno() == null) {
            if ("MANANA".equals(medicoDTO.getTurno())) {
                medico.setHoraInicioTurno(java.time.LocalTime.of(7, 0));
                medico.setHoraFinTurno(java.time.LocalTime.of(14, 0));
            } else if ("TARDE".equals(medicoDTO.getTurno())) {
                medico.setHoraInicioTurno(java.time.LocalTime.of(13, 0));
                medico.setHoraFinTurno(java.time.LocalTime.of(19, 0));
            }
        } else {
            medico.setHoraInicioTurno(medicoDTO.getHoraInicioTurno());
            medico.setHoraFinTurno(medicoDTO.getHoraFinTurno());
        }
        medico.setEspecialidad(especialidad);
        medico.setDni(medicoDTO.getDni());
        medico.setEmail(medicoDTO.getEmail());
        medico.setTelefono(medicoDTO.getTelefono());
        medico.setEstado(Medico.EstadoMedico.valueOf(medicoDTO.getEstado()));
        medico.setSexo(com.sisol.sys_citas.enums.Sexo.valueOf(medicoDTO.getSexo()));
        medico.setEstadoCivil(com.sisol.sys_citas.enums.EstadoCivil.fromDatabaseValue(medicoDTO.getEstadoCivil()));
        medico.setFechaNacimiento(medicoDTO.getFechaNacimiento());
        medico.setUsuario(usuario);

        return medicoRepository.save(medico);
    }
} 