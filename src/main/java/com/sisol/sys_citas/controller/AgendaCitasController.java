package com.sisol.sys_citas.controller;

import com.sisol.sys_citas.dto.UsuarioSesionDTO;
import com.sisol.sys_citas.model.Cita;
import com.sisol.sys_citas.model.Paciente;
import com.sisol.sys_citas.repository.CitaRepository;
import com.sisol.sys_citas.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;

@Controller
@RequestMapping("/agenda-citas")
public class AgendaCitasController {

    @Autowired
    private CitaRepository citaRepository;
    
    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public String mostrarAgendaCitas(HttpSession session, Model model) {
        System.out.println("=== AGENDA CITAS CONTROLLER EJECUTADO ===");
        System.out.println("=== MÉTODO mostrarAgendaCitas INICIADO ===");
        
        // Verificar si el usuario está logueado
        UsuarioSesionDTO usuario = (UsuarioSesionDTO) session.getAttribute("usuario");
        System.out.println("=== VERIFICANDO SESIÓN ===");
        System.out.println("Usuario en sesión: " + (usuario != null ? usuario.getCorreo() : "NULL"));
        
        if (usuario == null) {
            System.out.println("Usuario no logueado en agenda, redirigiendo...");
            return "redirect:/inicio?showLoginModal=true";
        }

        System.out.println("Usuario logueado en agenda: " + usuario.getCorreo());
        System.out.println("ID del usuario: " + usuario.getId());

        try {
            // Buscar el paciente asociado al usuario
            Optional<Paciente> pacienteOpt = pacienteRepository.findById(usuario.getId());
            if (!pacienteOpt.isPresent()) {
                System.out.println("ERROR: No se encontró paciente para el usuario ID: " + usuario.getId());
                model.addAttribute("error", "No se encontró información del paciente");
                return "pages/paciente/miscitas";
            }
            
            Paciente paciente = pacienteOpt.get();
            System.out.println("Paciente encontrado: " + paciente.getNombres() + " " + paciente.getApellidos());
            System.out.println("ID del paciente: " + paciente.getId());

            // Obtener todas las citas del paciente
            System.out.println("=== BUSCANDO CITAS EN BD ===");
            List<Cita> citas = citaRepository.findByPacienteIdWithRelations(paciente.getId());
            System.out.println("Citas encontradas: " + citas.size());
            
            // Ordenar las citas por fecha de disponibilidad (más recientes primero)
            citas.sort(Comparator.comparing(
                (Cita cita) -> cita.getDisponibilidad() != null ? cita.getDisponibilidad().getFechaDisponibilidad() : null,
                Comparator.nullsLast(Comparator.reverseOrder())
            ).thenComparing(
                (Cita cita) -> cita.getDisponibilidad() != null ? cita.getDisponibilidad().getHoraInicio() : null,
                Comparator.nullsLast(Comparator.reverseOrder())
            ));
            System.out.println("Citas ordenadas por fecha y hora");
            
            // Calcular estadísticas
            long totalCitas = citas.size();
            long citasPendientes = citas.stream().filter(c -> "CONFIRMADA".equals(c.getEstado())).count();
            long citasCompletadas = citas.stream().filter(c -> "COMPLETADA".equals(c.getEstado())).count();
            long citasCanceladas = citas.stream().filter(c -> "CANCELADA".equals(c.getEstado())).count();

            // Agregar datos al modelo
            System.out.println("=== AGREGANDO DATOS AL MODELO ===");
            model.addAttribute("usuario", usuario);
            model.addAttribute("citas", citas);
            model.addAttribute("totalCitas", totalCitas);
            model.addAttribute("citasPendientes", citasPendientes);
            model.addAttribute("citasCompletadas", citasCompletadas);
            model.addAttribute("citasCanceladas", citasCanceladas);

            System.out.println("Estadísticas - Total: " + totalCitas + 
                             ", Pendientes: " + citasPendientes + 
                             ", Completadas: " + citasCompletadas + 
                             ", Canceladas: " + citasCanceladas);

        } catch (Exception e) {
            System.err.println("Error en AgendaCitasController: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar las citas: " + e.getMessage());
        }

        System.out.println("Retornando vista: pages/paciente/miscitas");
        return "pages/paciente/miscitas";
    }
} 