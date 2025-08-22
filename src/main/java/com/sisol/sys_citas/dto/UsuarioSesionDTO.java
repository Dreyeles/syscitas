package com.sisol.sys_citas.dto;

import com.sisol.sys_citas.enums.Rol;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@Getter
@Setter
@ToString
public class UsuarioSesionDTO {
    private Long id;
    private String nombres;
    private String correo; // ¡Cambiado a 'correo' con minúscula!
    private String contrasenia;
    private Rol rol;
    private String apellidos;

    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
}