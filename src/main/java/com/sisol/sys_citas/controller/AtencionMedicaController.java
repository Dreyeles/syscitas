package com.sisol.sys_citas.controller;

import com.sisol.sys_citas.dto.AtencionMedicaDTO;
import com.sisol.sys_citas.service.AtencionMedicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atencion-medica")
@CrossOrigin(origins = "*")
public class AtencionMedicaController {

    @Autowired
    private AtencionMedicaService atencionMedicaService;

    // Crear nueva atención médica
    @PostMapping
    public ResponseEntity<AtencionMedicaDTO> crearAtencionMedica(@RequestBody AtencionMedicaDTO dto) {
        try {
            AtencionMedicaDTO creada = atencionMedicaService.crearAtencionMedica(dto);
            return ResponseEntity.ok(creada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Actualizar atención médica
    @PutMapping("/{id}")
    public ResponseEntity<AtencionMedicaDTO> actualizarAtencionMedica(
            @PathVariable Long id, 
            @RequestBody AtencionMedicaDTO dto) {
        try {
            AtencionMedicaDTO actualizada = atencionMedicaService.actualizarAtencionMedica(id, dto);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Marcar como realizada
    @PutMapping("/{id}/realizar")
    public ResponseEntity<AtencionMedicaDTO> marcarComoRealizada(@PathVariable Long id) {
        try {
            AtencionMedicaDTO realizada = atencionMedicaService.marcarComoRealizada(id);
            return ResponseEntity.ok(realizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<AtencionMedicaDTO> obtenerPorId(@PathVariable Long id) {
        try {
            AtencionMedicaDTO atencion = atencionMedicaService.obtenerPorId(id);
            return ResponseEntity.ok(atencion);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener por cita
    @GetMapping("/cita/{citaId}")
    public ResponseEntity<AtencionMedicaDTO> obtenerPorCita(@PathVariable Long citaId) {
        AtencionMedicaDTO atencion = atencionMedicaService.obtenerPorCita(citaId);
        if (atencion != null) {
            return ResponseEntity.ok(atencion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener historial clínico de un paciente
    @GetMapping("/historial/{pacienteId}")
    public ResponseEntity<List<AtencionMedicaDTO>> obtenerHistorialClinico(@PathVariable Long pacienteId) {
        try {
            List<AtencionMedicaDTO> historial = atencionMedicaService.obtenerHistorialClinico(pacienteId);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener atenciones por médico
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<AtencionMedicaDTO>> obtenerPorMedico(@PathVariable Long medicoId) {
        try {
            List<AtencionMedicaDTO> atenciones = atencionMedicaService.obtenerPorMedico(medicoId);
            return ResponseEntity.ok(atenciones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener todas las atenciones
    @GetMapping
    public ResponseEntity<List<AtencionMedicaDTO>> obtenerTodas() {
        try {
            List<AtencionMedicaDTO> atenciones = atencionMedicaService.obtenerTodas();
            return ResponseEntity.ok(atenciones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Eliminar atención médica
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAtencionMedica(@PathVariable Long id) {
        try {
            atencionMedicaService.eliminarAtencionMedica(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 