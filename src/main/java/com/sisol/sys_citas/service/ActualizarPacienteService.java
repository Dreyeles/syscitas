package com.sisol.sys_citas.service;

import com.sisol.sys_citas.dto.ActualizarPacienteDTO;
import com.sisol.sys_citas.model.Paciente;
import com.sisol.sys_citas.model.Usuario;
import com.sisol.sys_citas.repository.PacienteRepository;
import com.sisol.sys_citas.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ActualizarPacienteService {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;

    public ActualizarPacienteService(UsuarioRepository usuarioRepository, PacienteRepository pacienteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public Paciente actualizarDatosPaciente(Long pacienteId, ActualizarPacienteDTO actualizarDTO) {
        // 1. Buscar el paciente
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(pacienteId);
        if (!pacienteOpt.isPresent()) {
            throw new RuntimeException("No se encontró el paciente con ID: " + pacienteId);
        }

        Paciente paciente = pacienteOpt.get();
        Usuario usuario = paciente.getUsuario();

        // 2. Validar que el email no esté en uso por otro usuario
        if (!actualizarDTO.getCorreo().equals(usuario.getCorreo())) {
            Optional<Usuario> usuarioExistente = usuarioRepository.findByCorreo(actualizarDTO.getCorreo());
            if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(usuario.getId())) {
                throw new RuntimeException("El email '" + actualizarDTO.getCorreo() + "' ya está en uso por otro usuario.");
            }
        }

        // 3. Actualizar datos del usuario
        usuario.setCorreo(actualizarDTO.getCorreo());
        usuario.setFechaActualizacion(LocalDateTime.now());
        usuarioRepository.save(usuario);

        // 4. Actualizar datos del paciente
        paciente.setNombres(actualizarDTO.getNombres());
        paciente.setApellidos(actualizarDTO.getApellidos());
        paciente.setTelefono(actualizarDTO.getTelefono());
        paciente.setDireccion(actualizarDTO.getDireccion());
        paciente.setDistrito(actualizarDTO.getDistrito());
        paciente.setEstadoCivil(actualizarDTO.getEstadoCivil());
        paciente.setContactoEmergenciaNombre(actualizarDTO.getContactoEmergenciaNombre());
        paciente.setContactoEmergenciaTelefono(actualizarDTO.getContactoEmergenciaTelefono());

        return pacienteRepository.save(paciente);
    }
} 