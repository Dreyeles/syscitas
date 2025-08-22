package com.sisol.sys_citas.dto;

import java.time.LocalDate;

public class PacienteListaDTO {
    
    private Long id;
    private String dni;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String estadoCivil;
    private String direccion;
    private String distrito;
    private String contactoEmergenciaNombre;
    private String contactoEmergenciaTelefono;
    private String contactoEmergenciaParentesco;
    private LocalDate fechaNacimiento;
    private String sexo;
    
    // Constructor por defecto
    public PacienteListaDTO() {}
    
    // Constructor completo
    public PacienteListaDTO(Long id, String dni, String nombres, String apellidos, 
                           String telefono, String estadoCivil, String direccion, 
                           String distrito, String contactoEmergenciaNombre, 
                           String contactoEmergenciaTelefono, String contactoEmergenciaParentesco, 
                           LocalDate fechaNacimiento, String sexo) {
        this.id = id;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.estadoCivil = estadoCivil;
        this.direccion = direccion;
        this.distrito = distrito;
        this.contactoEmergenciaNombre = contactoEmergenciaNombre;
        this.contactoEmergenciaTelefono = contactoEmergenciaTelefono;
        this.contactoEmergenciaParentesco = contactoEmergenciaParentesco;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }
    
    public String getContactoEmergenciaNombre() { return contactoEmergenciaNombre; }
    public void setContactoEmergenciaNombre(String contactoEmergenciaNombre) { this.contactoEmergenciaNombre = contactoEmergenciaNombre; }
    
    public String getContactoEmergenciaTelefono() { return contactoEmergenciaTelefono; }
    public void setContactoEmergenciaTelefono(String contactoEmergenciaTelefono) { this.contactoEmergenciaTelefono = contactoEmergenciaTelefono; }
    
    public String getContactoEmergenciaParentesco() { return contactoEmergenciaParentesco; }
    public void setContactoEmergenciaParentesco(String contactoEmergenciaParentesco) { this.contactoEmergenciaParentesco = contactoEmergenciaParentesco; }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
}
