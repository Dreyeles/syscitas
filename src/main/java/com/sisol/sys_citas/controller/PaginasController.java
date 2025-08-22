package com.sisol.sys_citas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginasController {

    // Página de atención médica para médicos
    @GetMapping("/medico/atencion-medica")
    public String atencionMedica() {
        return "pages/medico/atencion-medica";
    }

    // Página de historia clínica para personal administrativo
    @GetMapping("/personal-administrativo/historia-clinica")
    public String historiaClinica() {
        return "pages/personal_administrativo/historia-clinica";
    }
} 