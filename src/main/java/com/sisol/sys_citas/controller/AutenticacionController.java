package com.sisol.sys_citas.controller;

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
import com.sisol.sys_citas.enums.GrupoSanguineo;
import com.sisol.sys_citas.enums.Sexo;
import com.sisol.sys_citas.service.RegistroService;
import com.sisol.sys_citas.exceptions.RegistroException; // Importar tu excepción personalizada

@Controller
@RequestMapping("/auth")
public class AutenticacionController {

    private final RegistroService registroService;

    public AutenticacionController(RegistroService registroService) {
        this.registroService = registroService;
    }

    // --- MÉTODO PARA LA PÁGINA DE LOGIN ---
    @GetMapping("/login") // Este método manejará las solicitudes GET a /auth/login
    public String showLoginForm(Model model) {
        // Al devolver "fragments/login", Thymeleaf buscará el archivo en
        // src/main/resources/templates/fragments/login.html
        return "fragments/login";
    }
    // --- FIN DEL MÉTODO DE LOGIN ---

    // --- MÉTODOS DE REGISTRO DE PACIENTES ---

    // Este método manejará las solicitudes GET a /auth/registro/mostrar
    @GetMapping("/registro/mostrar")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("registroUsuarioDTO")) {
            model.addAttribute("registroUsuarioDTO", new RegistroUsuarioDTO());
        }
        model.addAttribute("sexos", Sexo.values());
        model.addAttribute("estadosCiviles", EstadoCivil.values());
        model.addAttribute("gruposSanguineos", GrupoSanguineo.values());

        return "registrousuario";
    }

    @PostMapping("/registrar")
    public String registerUser(@Valid @ModelAttribute("registroUsuarioDTO") RegistroUsuarioDTO registroUsuarioDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        // Validaciones iniciales del DTO (anotaciones @Valid)
        if (bindingResult.hasErrors()) {
            // Si hay errores de validación del DTO, se vuelve al formulario
            model.addAttribute("sexos", Sexo.values());
            model.addAttribute("estadosCiviles", EstadoCivil.values());
            model.addAttribute("gruposSanguineos", GrupoSanguineo.values());
            return "registrousuario";
        }

        // Validación de coincidencia de contraseñas
        if (!registroUsuarioDTO.contraseniasCoinciden()) {
            bindingResult.rejectValue("confirmarContrasenia", "error.registroUsuarioDTO", "Las contraseñas no coinciden.");
        }

        // Re-check de errores después de la validación de contraseñas
        if (bindingResult.hasErrors()) {
            model.addAttribute("sexos", Sexo.values());
            model.addAttribute("estadosCiviles", EstadoCivil.values());
            model.addAttribute("gruposSanguineos", GrupoSanguineo.values());
            return "registrousuario";
        }

        try {
            registroService.registrarNuevoPaciente(registroUsuarioDTO);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Registro de paciente exitoso! Ahora puedes iniciar sesión.");
            return "redirect:/auth/login";
        } catch (RegistroException e) { // Cambiado de IllegalStateException a RegistroException
            bindingResult.reject("error.registroUsuarioDTO", e.getMessage()); // Añade el mensaje de error al BindingResult
            model.addAttribute("sexos", Sexo.values());
            model.addAttribute("estadosCiviles", EstadoCivil.values());
            model.addAttribute("gruposSanguineos", GrupoSanguineo.values());
            return "registrousuario";
        } catch (Exception e) {
            // Para cualquier otro error inesperado no cubierto por RegistroException
            bindingResult.reject("error.registroUsuarioDTO", "Ocurrió un error inesperado al registrar: " + e.getMessage());
            e.printStackTrace(); // Imprime la traza de la pila para depuración
            model.addAttribute("sexos", Sexo.values());
            model.addAttribute("estadosCiviles", EstadoCivil.values());
            model.addAttribute("gruposSanguineos", GrupoSanguineo.values());
            return "registrousuario";
        }
    }
    // --- FIN MÉTODOS DE REGISTRO DE PACIENTES ---
}