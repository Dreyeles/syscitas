package com.sisol.sys_citas.security;

import com.sisol.sys_citas.model.Usuario;
import com.sisol.sys_citas.model.Paciente;
import com.sisol.sys_citas.repository.UsuarioRepository;
import com.sisol.sys_citas.dto.UsuarioSesionDTO;
import com.sisol.sys_citas.enums.Rol;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.sisol.sys_citas.mapper.AuthMapper;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AuthMapper authMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("¡Entró al método onAuthenticationSuccess!");
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(username).orElse(null);
        
        if (usuario != null) {
            // Usar la misma lógica que AutenticacionService - obtener el DTO completo con nombre
            UsuarioSesionDTO usuarioSesionDTO = authMapper.verificarCorreo(usuario.getCorreo());
            
            // Obtener nombre completo según el rol (misma lógica que AutenticacionService)
            String nombreCompleto = null;
            switch (usuario.getRol()) {
                case ROLE_PACIENTE:
                    nombreCompleto = authMapper.obtenerNombrePaciente(usuario.getId());
                    break;
                case ROLE_MEDICO:
                    nombreCompleto = authMapper.obtenerNombreMedico(usuario.getId());
                    break;
                case ROLE_PERSONAL_ADMINISTRATIVO:
                    nombreCompleto = authMapper.obtenerNombrePersonalAdministrativo(usuario.getId());
                    break;
                default:
                    nombreCompleto = "Usuario";
            }
            usuarioSesionDTO.setNombres(nombreCompleto);
            
            request.getSession().setAttribute("usuario", usuarioSesionDTO);
            System.out.println("Usuario guardado en sesión tras login exitoso: nombres='" + nombreCompleto + "', correo='" + username + "', rol=" + usuario.getRol());
        } else {
            System.out.println("No se encontró el usuario tras login exitoso: correo='" + username + "'");
        }
        
        // Redirección según el rol (misma lógica para todos)
        if (usuario != null) {
            switch (usuario.getRol()) {
                case ROLE_PERSONAL_ADMINISTRATIVO:
                case ROLE_MEDICO:
                    response.sendRedirect("/dashboard");
                    break;
                case ROLE_PACIENTE:
                default:
                    response.sendRedirect("/inicio");
                    break;
            }
        } else {
            response.sendRedirect("/inicio");
        }
    }
} 