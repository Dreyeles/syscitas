package com.sisol.sys_citas.service;

import com.sisol.sys_citas.clients.reciec.dto.ResponseClientReniecDTO;
import com.sisol.sys_citas.dto.RegistroPacienteDTO;

public interface PacienteService {
    ResponseClientReniecDTO validarDni(String dni);
    void registrarPaciente(RegistroPacienteDTO dto);
} 
