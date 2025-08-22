package com.sisol.sys_citas.converter;

import com.sisol.sys_citas.enums.Sexo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SexoConverter implements AttributeConverter<Sexo, String> {

    @Override
    public String convertToDatabaseColumn(Sexo sexo) {
        if (sexo == null) {
            return null;
        }
        return sexo.getDisplayName();
    }

    @Override
    public Sexo convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        
        for (Sexo sex : Sexo.values()) {
            if (sex.getDisplayName().equals(dbData)) {
                return sex;
            }
        }
        
        throw new IllegalArgumentException("Sexo no válido: " + dbData);
    }
} 