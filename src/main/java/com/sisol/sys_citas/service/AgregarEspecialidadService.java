package com.sisol.sys_citas.service;

import com.sisol.sys_citas.dto.AgregarEspecialidadDTO;
import com.sisol.sys_citas.model.Especialidad;
import com.sisol.sys_citas.repository.EspecialidadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgregarEspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    public AgregarEspecialidadService(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    @Transactional
    public Especialidad agregarEspecialidad(AgregarEspecialidadDTO especialidadDTO) {
        // Validar que el nombre no esté ya registrado
        if (especialidadRepository.existsByNombre(especialidadDTO.getNombre())) {
            throw new RuntimeException("La especialidad '" + especialidadDTO.getNombre() + "' ya está registrada.");
        }

        // Crear y guardar la entidad Especialidad
        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(especialidadDTO.getNombre());
        especialidad.setDescripcion(especialidadDTO.getDescripcion());

        return especialidadRepository.save(especialidad);
    }
} 