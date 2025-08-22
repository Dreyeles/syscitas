package com.sisol.sys_citas.controller;

import com.sisol.sys_citas.dto.CancelarCitaDTO;
import com.sisol.sys_citas.dto.CancelarCitaResponseDTO;
import com.sisol.sys_citas.dto.ReagendarCitaDTO;
import com.sisol.sys_citas.dto.ReagendarCitaResponseDTO;
import com.sisol.sys_citas.service.CitasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitasRestController {

    @Autowired
    private CitasService citasService;

    @PostMapping("/cancelar")
    public ResponseEntity<CancelarCitaResponseDTO> cancelarCita(@RequestBody CancelarCitaDTO cancelarCitaDTO) {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String correo = authentication.getName();
            
            // Cancelar la cita
            citasService.cancelarCita(cancelarCitaDTO.getNumTicket(), correo, cancelarCitaDTO.getMotivoCancelacion());
            
            CancelarCitaResponseDTO response = new CancelarCitaResponseDTO(
                "Cita cancelada exitosamente. El horario ha quedado disponible para otros pacientes.",
                true
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            CancelarCitaResponseDTO errorResponse = new CancelarCitaResponseDTO(
                "Error al cancelar la cita: " + e.getMessage(),
                false
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/reagendar")
    public ResponseEntity<ReagendarCitaResponseDTO> reagendarCita(@RequestBody ReagendarCitaDTO reagendarCitaDTO) {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String correo = authentication.getName();
            
            // Reagendar la cita
            ReagendarCitaResponseDTO response = citasService.reagendarCita(
                reagendarCitaDTO.getNumTicket(), 
                correo, 
                reagendarCitaDTO.getNuevaDisponibilidadId()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ReagendarCitaResponseDTO errorResponse = new ReagendarCitaResponseDTO(
                "Error al reagendar la cita: " + e.getMessage(),
                false
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
