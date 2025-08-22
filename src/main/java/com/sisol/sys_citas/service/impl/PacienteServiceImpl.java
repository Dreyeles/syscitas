package com.sisol.sys_citas.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sisol.sys_citas.clients.reciec.dto.ResponseClientReniecDTO;
import com.sisol.sys_citas.clients.reciec.service.ClientReniecApiService;
import com.sisol.sys_citas.service.PacienteService;
import com.sisol.sys_citas.dto.RegistroPacienteDTO;
import com.sisol.sys_citas.model.Paciente;
import com.sisol.sys_citas.model.Usuario;
import com.sisol.sys_citas.repository.PacienteRepository;
import com.sisol.sys_citas.repository.UsuarioRepository;
import com.sisol.sys_citas.enums.Rol;
import com.sisol.sys_citas.enums.Sexo;
import com.sisol.sys_citas.enums.EstadoCivil;
import java.time.LocalDateTime;

@Service
public class PacienteServiceImpl implements PacienteService {

    private final ClientReniecApiService clientReniecApiService;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    
    public PacienteServiceImpl(ClientReniecApiService clientReniecApiService,
                              PacienteRepository pacienteRepository,
                              UsuarioRepository usuarioRepository) {
        this.clientReniecApiService = clientReniecApiService;
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public ResponseClientReniecDTO validarDni(String dni) {
        return clientReniecApiService.identificarPersonaPorDni(dni);
    }
    
    @Override
    @Transactional
    public void registrarPaciente(RegistroPacienteDTO dto) {
        // Validar que el DNI no exista
        if (pacienteRepository.existsByDni(dto.getDni())) {
            throw new RuntimeException("Ya existe un paciente con ese DNI");
        }
        
        // Crear usuario temporal (sin contraseña para este caso)
        Usuario usuario = new Usuario();
        usuario.setCorreo("temp_" + dto.getDni() + "@sisol.com"); // Email temporal
        usuario.setContrasenia("temp123"); // Contraseña temporal
        usuario.setRol(Rol.ROLE_PACIENTE);
        usuario.setActivo(1);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setFechaActualizacion(LocalDateTime.now());
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        // Crear paciente
        Paciente paciente = new Paciente();
        paciente.setNombres(dto.getNombres());
        paciente.setApellidos(dto.getApellidos());
        paciente.setDni(dto.getDni());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setSexo(Sexo.valueOf(dto.getSexo()));
        paciente.setEstadoCivil(EstadoCivil.valueOf(dto.getEstadoCivil()));
        paciente.setTelefono(dto.getTelefono());
        paciente.setDireccion(dto.getDireccion());
        paciente.setDistrito(dto.getDistrito());
        paciente.setContactoEmergenciaNombre(dto.getContactoEmergenciaNombre());
        paciente.setContactoEmergenciaTelefono(dto.getContactoEmergenciaTelefono());
        paciente.setContactoEmergenciaParentesco(dto.getContactoEmergenciaParentesco());
        paciente.setUsuario(usuarioGuardado);
        
        pacienteRepository.save(paciente);
    }
}
