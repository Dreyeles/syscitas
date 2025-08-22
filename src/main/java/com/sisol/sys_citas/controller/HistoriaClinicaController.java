package com.sisol.sys_citas.controller;

import com.sisol.sys_citas.dto.HistoriaClinicaDTO;
import com.sisol.sys_citas.service.HistoriaClinicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historia-clinica")
@CrossOrigin(origins = "*")
public class HistoriaClinicaController {

    @Autowired
    private HistoriaClinicaService historiaClinicaService;

    // Crear nueva historia clínica
    @PostMapping
    public ResponseEntity<HistoriaClinicaDTO> crearHistoriaClinica(@RequestBody HistoriaClinicaDTO dto) {
        try {
            HistoriaClinicaDTO creada = historiaClinicaService.crearHistoriaClinica(dto);
            return ResponseEntity.ok(creada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Actualizar historia clínica
    @PutMapping("/{id}")
    public ResponseEntity<HistoriaClinicaDTO> actualizarHistoriaClinica(
            @PathVariable Long id, 
            @RequestBody HistoriaClinicaDTO dto) {
        try {
            HistoriaClinicaDTO actualizada = historiaClinicaService.actualizarHistoriaClinica(id, dto);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<HistoriaClinicaDTO> obtenerPorId(@PathVariable Long id) {
        try {
            HistoriaClinicaDTO historia = historiaClinicaService.obtenerPorId(id);
            return ResponseEntity.ok(historia);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener por paciente
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<HistoriaClinicaDTO> obtenerPorPaciente(@PathVariable Long pacienteId) {
        HistoriaClinicaDTO historia = historiaClinicaService.obtenerPorPaciente(pacienteId);
        if (historia != null) {
            return ResponseEntity.ok(historia);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Verificar si existe historia clínica para un paciente
    @GetMapping("/existe/{pacienteId}")
    public ResponseEntity<Boolean> existeHistoriaClinica(@PathVariable Long pacienteId) {
        boolean existe = historiaClinicaService.existeHistoriaClinica(pacienteId);
        return ResponseEntity.ok(existe);
    }

    // Obtener todas las historias clínicas
    @GetMapping
    public ResponseEntity<List<HistoriaClinicaDTO>> obtenerTodas() {
        try {
            List<HistoriaClinicaDTO> historias = historiaClinicaService.obtenerTodas();
            return ResponseEntity.ok(historias);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Eliminar historia clínica
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHistoriaClinica(@PathVariable Long id) {
        try {
            historiaClinicaService.eliminarHistoriaClinica(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 