package com.sisol.sys_citas.service;

import com.sisol.sys_citas.model.HistoriaClinica;
import com.sisol.sys_citas.model.Paciente;
import com.sisol.sys_citas.dto.HistoriaClinicaDTO;
import com.sisol.sys_citas.repository.HistoriaClinicaRepository;
import com.sisol.sys_citas.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class HistoriaClinicaService {

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // Crear nueva historia clínica
    public HistoriaClinicaDTO crearHistoriaClinica(HistoriaClinicaDTO dto) {
        // Validar que el paciente existe
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(dto.getPacienteId());
        if (!pacienteOpt.isPresent()) {
            throw new RuntimeException("El paciente no existe");
        }

        // Verificar que no existe ya una historia clínica para este paciente
        if (historiaClinicaRepository.existsByPacienteId(dto.getPacienteId())) {
            throw new RuntimeException("Ya existe una historia clínica para este paciente");
        }

        HistoriaClinica historiaClinica = new HistoriaClinica();
        historiaClinica.setPaciente(pacienteOpt.get());
        historiaClinica.setGrupoSanguineo(dto.getGrupoSanguineo());
        historiaClinica.setAlergias(dto.getAlergias());
        historiaClinica.setEnfermedadesCronicas(dto.getEnfermedadesCronicas());
        historiaClinica.setAntecedentesPersonales(dto.getAntecedentesPersonales());
        historiaClinica.setAntecedentesFamiliares(dto.getAntecedentesFamiliares());
        historiaClinica.setMedicacionActual(dto.getMedicacionActual());
        historiaClinica.setDiscapacidad(dto.getDiscapacidad());
        historiaClinica.setVacunacion(dto.getVacunacion());

        HistoriaClinica saved = historiaClinicaRepository.save(historiaClinica);
        return convertirADTO(saved);
    }

    // Actualizar historia clínica
    public HistoriaClinicaDTO actualizarHistoriaClinica(Long id, HistoriaClinicaDTO dto) {
        Optional<HistoriaClinica> historiaOpt = historiaClinicaRepository.findById(id);
        if (!historiaOpt.isPresent()) {
            throw new RuntimeException("La historia clínica no existe");
        }

        HistoriaClinica historia = historiaOpt.get();
        historia.setGrupoSanguineo(dto.getGrupoSanguineo());
        historia.setAlergias(dto.getAlergias());
        historia.setEnfermedadesCronicas(dto.getEnfermedadesCronicas());
        historia.setAntecedentesPersonales(dto.getAntecedentesPersonales());
        historia.setAntecedentesFamiliares(dto.getAntecedentesFamiliares());
        historia.setMedicacionActual(dto.getMedicacionActual());
        historia.setDiscapacidad(dto.getDiscapacidad());
        historia.setVacunacion(dto.getVacunacion());

        HistoriaClinica saved = historiaClinicaRepository.save(historia);
        return convertirADTO(saved);
    }

    // Obtener por ID
    public HistoriaClinicaDTO obtenerPorId(Long id) {
        Optional<HistoriaClinica> historiaOpt = historiaClinicaRepository.findById(id);
        if (!historiaOpt.isPresent()) {
            throw new RuntimeException("La historia clínica no existe");
        }
        return convertirADTO(historiaOpt.get());
    }

    // Obtener por paciente
    public HistoriaClinicaDTO obtenerPorPaciente(Long pacienteId) {
        Optional<HistoriaClinica> historiaOpt = historiaClinicaRepository.findByPacienteId(pacienteId);
        if (!historiaOpt.isPresent()) {
            return null;
        }
        return convertirADTO(historiaOpt.get());
    }

    // Obtener todas las historias clínicas
    public List<HistoriaClinicaDTO> obtenerTodas() {
        List<HistoriaClinica> historias = historiaClinicaRepository.findAll();
        return historias.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Verificar si existe historia clínica para un paciente
    public boolean existeHistoriaClinica(Long pacienteId) {
        return historiaClinicaRepository.existsByPacienteId(pacienteId);
    }

    // Eliminar historia clínica
    public void eliminarHistoriaClinica(Long id) {
        if (!historiaClinicaRepository.existsById(id)) {
            throw new RuntimeException("La historia clínica no existe");
        }
        historiaClinicaRepository.deleteById(id);
    }

    // Método privado para convertir entidad a DTO
    private HistoriaClinicaDTO convertirADTO(HistoriaClinica historia) {
        HistoriaClinicaDTO dto = new HistoriaClinicaDTO();
        dto.setId(historia.getId());
        dto.setPacienteId(historia.getPaciente().getId());
        dto.setGrupoSanguineo(historia.getGrupoSanguineo());
        dto.setAlergias(historia.getAlergias());
        dto.setEnfermedadesCronicas(historia.getEnfermedadesCronicas());
        dto.setAntecedentesPersonales(historia.getAntecedentesPersonales());
        dto.setAntecedentesFamiliares(historia.getAntecedentesFamiliares());
        dto.setMedicacionActual(historia.getMedicacionActual());
        dto.setDiscapacidad(historia.getDiscapacidad());
        dto.setVacunacion(historia.getVacunacion());
        dto.setFechaCreacion(historia.getFechaCreacion());
        dto.setFechaActualizacion(historia.getFechaActualizacion());
        
        // Información adicional del paciente
        dto.setNombrePaciente(historia.getPaciente().getNombres() + " " + 
                             historia.getPaciente().getApellidos());
        
        return dto;
    }
} 