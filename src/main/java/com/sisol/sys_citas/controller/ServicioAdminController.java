package com.sisol.sys_citas.controller;

import com.sisol.sys_citas.dto.AgregarServicioDTO;
import com.sisol.sys_citas.dto.UsuarioSesionDTO;
import com.sisol.sys_citas.repository.EspecialidadRepository;
import com.sisol.sys_citas.service.AgregarServicioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/servicios")
public class ServicioAdminController {

    private final AgregarServicioService agregarServicioService;
    private final EspecialidadRepository especialidadRepository;

    public ServicioAdminController(AgregarServicioService agregarServicioService,
                                   EspecialidadRepository especialidadRepository) {
        this.agregarServicioService = agregarServicioService;
        this.especialidadRepository = especialidadRepository;
    }

    @GetMapping("/agregar")
    public String verAgregarServicio() {
        return "redirect:/dashboard?tab=agregar-servicio";
    }

    @PostMapping("/agregar")
    public String agregarServicio(@Valid @org.springframework.web.bind.annotation.ModelAttribute("agregarServicioDTO") AgregarServicioDTO agregarServicioDTO,
                                  BindingResult bindingResult,
                                  Model model,
                                  HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mensajeError", "Por favor corrija los errores del formulario.");
            model.addAttribute("especialidades", especialidadRepository.findAll());
            model.addAttribute("agregarServicioDTO", agregarServicioDTO);
            UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
            if (usuarioSesion != null) {
                model.addAttribute("usuario", usuarioSesion);
            }
            return "dashboard";
        }

        try {
            agregarServicioService.agregarServicio(agregarServicioDTO);
            return "redirect:/dashboard?tab=agregar-servicio&ok=1";
        } catch (RuntimeException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            model.addAttribute("especialidades", especialidadRepository.findAll());
            model.addAttribute("agregarServicioDTO", agregarServicioDTO);
            UsuarioSesionDTO usuarioSesion = (UsuarioSesionDTO) session.getAttribute("usuario");
            if (usuarioSesion != null) {
                model.addAttribute("usuario", usuarioSesion);
            }
            return "dashboard";
        }
    }
}


