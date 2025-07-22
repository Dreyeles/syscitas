package com.sisol.sys_citas.controller;

import java.util.HashMap;
import java.util.Map;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sisol.sys_citas.service.PacienteService;

@RestController
@RequestMapping("/paciente")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }


    
    @GetMapping("consultar/{dni}")
    public ResponseEntity<?> consultarDni(@PathVariable String dni) {
        try {
            return ResponseEntity.ok().body(pacienteService.validarDni(dni));

        } catch (RuntimeException ex) {

            Map<String, Object> error = new HashMap<>();
            error.put("error", ex.getMessage());

            return ResponseEntity.badRequest().body(error);

        }

    }

}
