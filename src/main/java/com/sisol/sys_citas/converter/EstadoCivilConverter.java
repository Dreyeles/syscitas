package com.sisol.sys_citas.converter;

import com.sisol.sys_citas.enums.EstadoCivil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EstadoCivilConverter implements AttributeConverter<EstadoCivil, String> {

    @Override
    public String convertToDatabaseColumn(EstadoCivil estadoCivil) {
        if (estadoCivil == null) {
            return null;
        }
        return estadoCivil.getDisplayName();
    }

    @Override
    public EstadoCivil convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        
        for (EstadoCivil estado : EstadoCivil.values()) {
            if (estado.getDisplayName().equals(dbData)) {
                return estado;
            }
        }
        
        throw new IllegalArgumentException("Estado civil no válido: " + dbData);
    }
} 