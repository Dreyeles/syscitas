package com.sisol.sys_citas.security;

import com.sisol.sys_citas.model.Usuario;
import com.sisol.sys_citas.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User; // Importa la clase User de Spring Security
import org.springframework.security.core.userdetails.UserDetails; // Importa la interfaz UserDetails
import org.springframework.security.core.userdetails.UserDetailsService; // Importa la interfaz UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Importa la excepción
import org.springframework.stereotype.Service;

import java.util.Collections; // Para manejar los roles/autoridades
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service // Marca esta clase como un componente de servicio de Spring
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository; // Inyecta tu repositorio de Usuario

    // Constructor para la inyección de dependencias
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // 1. Buscar el usuario en la base de datos por su correo
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

        // 2. Construir y devolver un objeto UserDetails de Spring Security
        // Este objeto contiene el nombre de usuario (correo), la contraseña y los roles/autoridades.
        String rol = usuario.getRol().name();
        System.out.println("Usuario encontrado: " + usuario.getCorreo() + " con rol: " + rol);
        
        return new User(
                usuario.getCorreo(),
                usuario.getContrasenia(), // La contraseña ya debe estar encriptada en la DB
                Collections.singletonList(new SimpleGrantedAuthority(rol)) // Mapear el rol del usuario
        );
    }
}