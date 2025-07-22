package com.sisol.sys_citas.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sisol.sys_citas.clients.reciec.dto.ResponseClientReniecDTO;
import com.sisol.sys_citas.clients.reciec.service.ClientReniecApiService;
import com.sisol.sys_citas.service.PacienteService;



@Service
public class PacienteServiceImpl implements PacienteService {

    private final ClientReniecApiService clientReniecApiService;
    
    public PacienteServiceImpl(ClientReniecApiService clientReniecApiService){
        this.clientReniecApiService = clientReniecApiService;
    }
    

    @Override
    @Transactional(readOnly = true)
    public ResponseClientReniecDTO validarDni(String dni) {

        // if (pacienteRepository.existsByDni(dni)) {
        //     throw new PacienteException("El DNI ya se encuentra registrado");
        // }
        return clientReniecApiService.identificarPersonaPorDni(dni);
    }

    






    
}
