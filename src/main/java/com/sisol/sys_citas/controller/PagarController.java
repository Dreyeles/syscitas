package com.sisol.sys_citas.controller;

import com.sisol.sys_citas.dto.UsuarioSesionDTO;
import com.sisol.sys_citas.model.Cita;
import com.sisol.sys_citas.model.Disponibilidad;
import com.sisol.sys_citas.model.Especialidad;
import com.sisol.sys_citas.model.Medico;
import com.sisol.sys_citas.model.Paciente;
import com.sisol.sys_citas.model.Pago;
import com.sisol.sys_citas.model.Servicio;
import com.sisol.sys_citas.repository.CitaRepository;
import com.sisol.sys_citas.repository.DisponibilidadRepository;
import com.sisol.sys_citas.repository.EspecialidadRepository;
import com.sisol.sys_citas.repository.MedicoRepository;
import com.sisol.sys_citas.repository.PacienteRepository;
import com.sisol.sys_citas.repository.PagoRepository;
import com.sisol.sys_citas.repository.ServicioRepository;
import com.sisol.sys_citas.enums.Comprobante;
import com.sisol.sys_citas.enums.EstadoPago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/pagar")
public class PagarController {

    @Autowired
    private CitaRepository citaRepository;
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private EspecialidadRepository especialidadRepository;
    
    @Autowired
    private ServicioRepository servicioRepository;
    
    @Autowired
    private MedicoRepository medicoRepository;
    
    @Autowired
    private DisponibilidadRepository disponibilidadRepository;
    
    @Autowired
    private PagoRepository pagoRepository;

    @GetMapping
    public String mostrarPaginaPago(HttpSession session, Model model) {
        // Verificar si el usuario está logueado
        UsuarioSesionDTO usuario = (UsuarioSesionDTO) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/inicio?showLoginModal=true";
        }
        
        return "pages/paciente/pagar";
    }

    @PostMapping("/procesar")
    public String procesarPago(
            @RequestParam("codigoTransaccion") String codigoTransaccion,
            @RequestParam("servicio") String servicioNombre,
            @RequestParam("medico") String medicoNombre,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            @RequestParam("emailFacturacion") String emailFacturacion,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        System.out.println("=== PROCESANDO PAGO ===");
        System.out.println("Código Transacción: " + codigoTransaccion);
        System.out.println("Servicio: " + servicioNombre);
        System.out.println("Médico: " + medicoNombre);
        System.out.println("Fecha: " + fecha);
        System.out.println("Horario: " + horario);
        System.out.println("Email: " + emailFacturacion);
        
        try {
            // Obtener usuario de la sesión
            UsuarioSesionDTO usuario = (UsuarioSesionDTO) session.getAttribute("usuario");
            if (usuario == null) {
                System.out.println("ERROR: Usuario no encontrado en sesión");
                return "redirect:/inicio?showLoginModal=true";
            }
            
            System.out.println("Usuario encontrado: " + usuario.getCorreo() + " (ID: " + usuario.getId() + ")");
            
            // Buscar el paciente por ID de usuario
            Optional<Paciente> pacienteOpt = pacienteRepository.findById(usuario.getId());
            if (!pacienteOpt.isPresent()) {
                System.out.println("ERROR: Paciente no encontrado para usuario ID: " + usuario.getId());
                redirectAttributes.addFlashAttribute("error", "Paciente no encontrado");
                return "redirect:/pagar";
            }
            
            Paciente paciente = pacienteOpt.get();
            System.out.println("Paciente encontrado: " + paciente.getNombres() + " " + paciente.getApellidos());
            
            // Buscar servicio por nombre
            System.out.println("Buscando servicio: " + servicioNombre);
            Optional<Servicio> servicioOpt = servicioRepository.findByNombre(servicioNombre);
            if (!servicioOpt.isPresent()) {
                System.out.println("ERROR: Servicio no encontrado: " + servicioNombre);
                redirectAttributes.addFlashAttribute("error", "Servicio no encontrado: " + servicioNombre);
                return "redirect:/pagar";
            }
            
            System.out.println("Servicio encontrado: " + servicioOpt.get().getNombre() + " (Precio: " + servicioOpt.get().getPrecioServicio() + ")");
            
            // Buscar médico por nombre completo
            System.out.println("Buscando médico: " + medicoNombre);
            String[] nombresMedico = medicoNombre.split(" ", 2);
            if (nombresMedico.length < 2) {
                System.out.println("ERROR: Formato de médico inválido: " + medicoNombre);
                redirectAttributes.addFlashAttribute("error", "Formato de médico inválido");
                return "redirect:/pagar";
            }
            
            Optional<Medico> medicoOpt = medicoRepository.findByNombresAndApellidos(
                nombresMedico[0], nombresMedico[1]);
            if (!medicoOpt.isPresent()) {
                System.out.println("ERROR: Médico no encontrado: " + medicoNombre);
                redirectAttributes.addFlashAttribute("error", "Médico no encontrado: " + medicoNombre);
                return "redirect:/pagar";
            }
            
            System.out.println("Médico encontrado: " + medicoOpt.get().getNombres() + " " + medicoOpt.get().getApellidos());
            
            // Buscar disponibilidad para el médico en la fecha y horario
            System.out.println("Buscando disponibilidad para médico: " + medicoOpt.get().getNombres() + " " + medicoOpt.get().getApellidos());
            System.out.println("Fecha: " + fecha + ", Horario: " + horario);
            
            LocalDate fechaCita = LocalDate.parse(fecha);
            LocalTime horarioCita = LocalTime.parse(horario, DateTimeFormatter.ofPattern("HH:mm"));
            
            Optional<Disponibilidad> disponibilidadOpt = disponibilidadRepository
                .findByMedicoAndFechaDisponibilidadAndHoraInicioAndDisponible(
                    medicoOpt.get(), fechaCita, horarioCita, true);
            
            if (!disponibilidadOpt.isPresent()) {
                System.out.println("ERROR: No se encontró disponibilidad para el médico en esa fecha y horario");
                redirectAttributes.addFlashAttribute("error", "Horario no disponible para el médico seleccionado");
                return "redirect:/pagar";
            }
            
            System.out.println("Disponibilidad encontrada: ID " + disponibilidadOpt.get().getId());
            
            // Crear el pago con precio real del servicio
            Pago pago = new Pago();
            pago.setMonto(servicioOpt.get().getPrecioServicio());
            pago.setFechaPago(LocalDateTime.now());
            pago.setCodigoTransaccion(Long.parseLong(codigoTransaccion));
            pago.setEstado(EstadoPago.CONFIRMADO);
            pago.setComprobante(Comprobante.BOLETA_VENTA);
            
            // Guardar el pago
            System.out.println("Guardando pago...");
            Pago pagoGuardado = pagoRepository.save(pago);
            System.out.println("Pago guardado con ID: " + pagoGuardado.getId());
            
            // Crear la cita con todas las relaciones
            System.out.println("Creando cita...");
            Cita cita = new Cita();
            cita.setPaciente(paciente);
            cita.setDisponibilidad(disponibilidadOpt.get());
            cita.setPago(pagoGuardado);
            cita.setServicio(servicioOpt.get());
            cita.setMedico(medicoOpt.get());
            cita.setEstado("CONFIRMADA");
            cita.setObservaciones("Cita agendada con pago. Código: " + codigoTransaccion + 
                                 ". Email: " + emailFacturacion);
            cita.setNumTicket(disponibilidadOpt.get().getNumTicket());
            // fechaCreacion y fechaActualizacion se establecen automáticamente en el constructor
            
            // Guardar la cita
            Cita citaGuardada = citaRepository.save(cita);
            System.out.println("Cita guardada con ID: " + citaGuardada.getId());
            
            // Marcar disponibilidad como ocupada
            Disponibilidad disponibilidad = disponibilidadOpt.get();
            disponibilidad.setDisponible(false);
            disponibilidadRepository.save(disponibilidad);
            
            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("mensajeExito", 
                "¡Cita agendada exitosamente! Código de transacción: " + codigoTransaccion);
            
            return "redirect:/inicio?citaAgendada=true";
            
        } catch (Exception e) {
            System.err.println("ERROR EN PROCESAMIENTO DE PAGO: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al procesar el pago: " + e.getMessage());
            return "redirect:/pagar";
        }
    }


} 