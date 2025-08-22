package com.sisol.sys_citas.controller;

import com.sisol.sys_citas.dto.RegistroMedicoDTO;
import com.sisol.sys_citas.service.RegistroMedicoService;
import com.sisol.sys_citas.repository.EspecialidadRepository;
import com.sisol.sys_citas.clients.reciec.exception.RegistroException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/medico")
public class RegistroMedicoController {

    private final RegistroMedicoService registroMedicoService;
    private final EspecialidadRepository especialidadRepository;

    public RegistroMedicoController(RegistroMedicoService registroMedicoService,
                                    EspecialidadRepository especialidadRepository) {
        this.registroMedicoService = registroMedicoService;
        this.especialidadRepository = especialidadRepository;
    }

    @PostMapping("/registrar")
    public String registrarMedico(@Valid @ModelAttribute("registroMedicoDTO") RegistroMedicoDTO registroMedicoDTO,
                                 BindingResult bindingResult,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            // Si hay errores de validación, agregar al modelo y retornar dashboard
            model.addAttribute("org.springframework.validation.BindingResult.registroMedicoDTO", bindingResult);
            model.addAttribute("registroMedicoDTO", registroMedicoDTO);
            model.addAttribute("especialidades", especialidadRepository.findAll());
            model.addAttribute("sexos", com.sisol.sys_citas.enums.Sexo.values());
            model.addAttribute("estadosCiviles", com.sisol.sys_citas.enums.EstadoCivil.values());
            return "dashboard";
        }

        try {
            registroMedicoService.registrarMedico(registroMedicoDTO);
            return "redirect:/dashboard?tab=medicos&ok=1";
        } catch (RegistroException e) {
            model.addAttribute("mensajeError", e.getMessage());
            model.addAttribute("registroMedicoDTO", registroMedicoDTO);
            model.addAttribute("especialidades", especialidadRepository.findAll());
            model.addAttribute("sexos", com.sisol.sys_citas.enums.Sexo.values());
            model.addAttribute("estadosCiviles", com.sisol.sys_citas.enums.EstadoCivil.values());
            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("mensajeError", "Ocurrió un error inesperado durante el registro. Por favor, inténtalo de nuevo más tarde.");
            model.addAttribute("registroMedicoDTO", registroMedicoDTO);
            model.addAttribute("especialidades", especialidadRepository.findAll());
            model.addAttribute("sexos", com.sisol.sys_citas.enums.Sexo.values());
            model.addAttribute("estadosCiviles", com.sisol.sys_citas.enums.EstadoCivil.values());
            return "dashboard";
        }
    }
} 