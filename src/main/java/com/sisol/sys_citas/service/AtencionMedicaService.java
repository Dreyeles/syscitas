package com.sisol.sys_citas.service;

import com.sisol.sys_citas.model.AtencionMedica;
import com.sisol.sys_citas.model.Cita;
import com.sisol.sys_citas.model.Medico;
import com.sisol.sys_citas.dto.AtencionMedicaDTO;
import com.sisol.sys_citas.enums.EstadoAtencion;
import com.sisol.sys_citas.repository.AtencionMedicaRepository;
import com.sisol.sys_citas.repository.CitaRepository;
import com.sisol.sys_citas.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AtencionMedicaService {

    @Autowired
    private AtencionMedicaRepository atencionMedicaRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    // Crear nueva atención médica
    public AtencionMedicaDTO crearAtencionMedica(AtencionMedicaDTO dto) {
        // Validar que la cita existe
        Optional<Cita> citaOpt = citaRepository.findById(dto.getCitaId());
        if (!citaOpt.isPresent()) {
            throw new RuntimeException("La cita no existe");
        }

        // Validar que el médico existe
        Optional<Medico> medicoOpt = medicoRepository.findById(dto.getMedicoId());
        if (!medicoOpt.isPresent()) {
            throw new RuntimeException("El médico no existe");
        }

        // Verificar que no existe ya una atención médica para esta cita
        Optional<AtencionMedica> atencionExistente = atencionMedicaRepository.findByCitaId(dto.getCitaId());
        if (atencionExistente.isPresent()) {
            throw new RuntimeException("Ya existe una atención médica para esta cita");
        }

        AtencionMedica atencionMedica = new AtencionMedica();
        atencionMedica.setCita(citaOpt.get());
        atencionMedica.setMedico(medicoOpt.get());
        atencionMedica.setMotivoConsulta(dto.getMotivoConsulta());
        atencionMedica.setDiagnostico(dto.getDiagnostico());
        atencionMedica.setTratamiento(dto.getTratamiento());
        atencionMedica.setRecomendaciones(dto.getRecomendaciones());
        atencionMedica.setFechaAtencion(LocalDateTime.now());
        atencionMedica.setEstado(EstadoAtencion.PENDIENTE);

        AtencionMedica saved = atencionMedicaRepository.save(atencionMedica);
        return convertirADTO(saved);
    }

    // Actualizar atención médica
    public AtencionMedicaDTO actualizarAtencionMedica(Long id, AtencionMedicaDTO dto) {
        Optional<AtencionMedica> atencionOpt = atencionMedicaRepository.findById(id);
        if (!atencionOpt.isPresent()) {
            throw new RuntimeException("La atención médica no existe");
        }

        AtencionMedica atencion = atencionOpt.get();
        atencion.setMotivoConsulta(dto.getMotivoConsulta());
        atencion.setDiagnostico(dto.getDiagnostico());
        atencion.setTratamiento(dto.getTratamiento());
        atencion.setRecomendaciones(dto.getRecomendaciones());
        atencion.setEstado(dto.getEstado());

        AtencionMedica saved = atencionMedicaRepository.save(atencion);
        return convertirADTO(saved);
    }

    // Marcar como realizada
    public AtencionMedicaDTO marcarComoRealizada(Long id) {
        Optional<AtencionMedica> atencionOpt = atencionMedicaRepository.findById(id);
        if (!atencionOpt.isPresent()) {
            throw new RuntimeException("La atención médica no existe");
        }

        AtencionMedica atencion = atencionOpt.get();
        atencion.setEstado(EstadoAtencion.REALIZADO);
        atencion.setFechaAtencion(LocalDateTime.now());

        AtencionMedica saved = atencionMedicaRepository.save(atencion);
        return convertirADTO(saved);
    }

    // Obtener por ID
    public AtencionMedicaDTO obtenerPorId(Long id) {
        Optional<AtencionMedica> atencionOpt = atencionMedicaRepository.findById(id);
        if (!atencionOpt.isPresent()) {
            throw new RuntimeException("La atención médica no existe");
        }
        return convertirADTO(atencionOpt.get());
    }

    // Obtener por cita
    public AtencionMedicaDTO obtenerPorCita(Long citaId) {
        Optional<AtencionMedica> atencionOpt = atencionMedicaRepository.findByCitaId(citaId);
        if (!atencionOpt.isPresent()) {
            return null;
        }
        return convertirADTO(atencionOpt.get());
    }

    // Obtener historial clínico de un paciente
    public List<AtencionMedicaDTO> obtenerHistorialClinico(Long pacienteId) {
        List<AtencionMedica> atenciones = atencionMedicaRepository.findHistorialClinicoByPacienteId(pacienteId);
        return atenciones.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener atenciones por médico
    public List<AtencionMedicaDTO> obtenerPorMedico(Long medicoId) {
        List<AtencionMedica> atenciones = atencionMedicaRepository.findByMedicoId(medicoId);
        return atenciones.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener todas las atenciones
    public List<AtencionMedicaDTO> obtenerTodas() {
        List<AtencionMedica> atenciones = atencionMedicaRepository.findAll();
        return atenciones.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Eliminar atención médica
    public void eliminarAtencionMedica(Long id) {
        if (!atencionMedicaRepository.existsById(id)) {
            throw new RuntimeException("La atención médica no existe");
        }
        atencionMedicaRepository.deleteById(id);
    }

    // Método privado para convertir entidad a DTO
    private AtencionMedicaDTO convertirADTO(AtencionMedica atencion) {
        AtencionMedicaDTO dto = new AtencionMedicaDTO();
        dto.setId(atencion.getId());
        dto.setCitaId(atencion.getCita().getId());
        dto.setMedicoId(atencion.getMedico().getId());
        dto.setMotivoConsulta(atencion.getMotivoConsulta());
        dto.setDiagnostico(atencion.getDiagnostico());
        dto.setTratamiento(atencion.getTratamiento());
        dto.setRecomendaciones(atencion.getRecomendaciones());
        dto.setFechaAtencion(atencion.getFechaAtencion());
        dto.setEstado(atencion.getEstado());
        
        // Información adicional
        dto.setNombrePaciente(atencion.getCita().getPaciente().getNombres() + " " + 
                             atencion.getCita().getPaciente().getApellidos());
        dto.setNombreMedico(atencion.getMedico().getNombres() + " " + 
                           atencion.getMedico().getApellidos());
        dto.setEspecialidad(atencion.getMedico().getEspecialidad().getNombre());
        
        return dto;
    }
} 