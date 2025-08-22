package com.sisol.sys_citas.service;

import com.sisol.sys_citas.dto.AgregarServicioDTO;
import com.sisol.sys_citas.model.Especialidad;
import com.sisol.sys_citas.model.Servicio;
import com.sisol.sys_citas.repository.EspecialidadRepository;
import com.sisol.sys_citas.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgregarServicioService {

    private final ServicioRepository servicioRepository;
    private final EspecialidadRepository especialidadRepository;

    public AgregarServicioService(ServicioRepository servicioRepository,
                                  EspecialidadRepository especialidadRepository) {
        this.servicioRepository = servicioRepository;
        this.especialidadRepository = especialidadRepository;
    }

    @Transactional
    public Servicio agregarServicio(AgregarServicioDTO dto) {
        servicioRepository.findByNombre(dto.getNombre()).ifPresent(s -> {
            throw new RuntimeException("El servicio '" + dto.getNombre() + "' ya está registrado.");
        });

        Especialidad especialidad = especialidadRepository.findById(dto.getEspecialidadId())
                .orElseThrow(() -> new RuntimeException("La especialidad seleccionada no existe."));

        Servicio servicio = new Servicio();
        servicio.setNombre(dto.getNombre());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setPrecioServicio(dto.getPrecioServicio());
        servicio.setEspecialidad(especialidad);

        return servicioRepository.save(servicio);
    }
}


