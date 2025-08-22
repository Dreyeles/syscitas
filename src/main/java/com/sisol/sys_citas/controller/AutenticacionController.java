package com.sisol.sys_citas.controller;

import com.sisol.sys_citas.clients.reciec.exception.RegistroException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import com.sisol.sys_citas.dto.RegistroUsuarioDTO;
import com.sisol.sys_citas.enums.EstadoCivil;
import com.sisol.sys_citas.enums.Sexo;
import com.sisol.sys_citas.service.RegistroService;

@Controller
@RequestMapping("/auth")
public class AutenticacionController {

    private final RegistroService registroService;

    public AutenticacionController(RegistroService registroService) {
        this.registroService = registroService;
    }

    // --- MÉTODO PARA LA PÁGINA DE LOGIN ---
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // Redirigir a la página de inicio con el modal de login abierto
        return "redirect:/inicio?showLoginModal=true";
    }
    // --- FIN DEL MÉTODO DE LOGIN ---

    // --- MÉTODOS DE REGISTRO DE PACIENTES ---

    @GetMapping("/registro/mostrar")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("registroUsuarioDTO")) {
            model.addAttribute("registroUsuarioDTO", new RegistroUsuarioDTO());
        }
        model.addAttribute("sexos", Sexo.values());
        model.addAttribute("estadosCiviles", EstadoCivil.values());
        model.addAttribute("parentescos", new String[]{"Conyuge", "Padre", "Madre", "Hijo", "Hermano", "Amigo", "Otro"});

        return "pages/paciente/registrousuario";
    }
    
    @GetMapping("/registro-paciente")
    public String showRegistrationFormAlternativo(Model model) {
        return showRegistrationForm(model);
    }

    @PostMapping("/registrar")
    public String registerUser(@Valid @ModelAttribute("registroUsuarioDTO") RegistroUsuarioDTO registroUsuarioDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        // Validar contraseñas antes de verificar bindingResult
        if (!registroUsuarioDTO.contraseniasCoinciden()) {
            bindingResult.rejectValue("confirmarContrasenia", "contrasenia.mismatch", "Las contraseñas no coinciden.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("sexos", Sexo.values());
            model.addAttribute("estadosCiviles", EstadoCivil.values());
            model.addAttribute("parentescos", new String[]{"Conyuge", "Padre", "Madre", "Hijo", "Hermano", "Amigo", "Otro"});
            return "pages/paciente/registrousuario";
        }

        try {
            registroService.registrarNuevoPaciente(registroUsuarioDTO);
            redirectAttributes.addFlashAttribute("mensajeExito", "Registro exitoso. Por favor inicie sesión.");
            return "redirect:/inicio?showLoginModal=true";
        } catch (RegistroException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("sexos", Sexo.values());
            model.addAttribute("estadosCiviles", EstadoCivil.values());
            model.addAttribute("parentescos", new String[]{"Conyuge", "Padre", "Madre", "Hijo", "Hermano", "Amigo", "Otro"});
            return "pages/paciente/registrousuario";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error inesperado durante el registro. Por favor, inténtalo de nuevo más tarde.");
            model.addAttribute("sexos", Sexo.values());
            model.addAttribute("estadosCiviles", EstadoCivil.values());
            model.addAttribute("parentescos", new String[]{"Conyuge", "Padre", "Madre", "Hijo", "Hermano", "Amigo", "Otro"});
            return "pages/paciente/registrousuario";
        }
    }
    // --- FIN MÉTODOS DE REGISTRO DE PACIENTES ---
}