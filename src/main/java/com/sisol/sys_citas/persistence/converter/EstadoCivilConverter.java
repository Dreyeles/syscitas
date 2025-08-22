package com.sisol.sys_citas.persistence.converter;

import com.sisol.sys_citas.enums.EstadoCivil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class EstadoCivilConverter implements AttributeConverter<EstadoCivil, String> {
    @Override
    public String convertToDatabaseColumn(EstadoCivil attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public EstadoCivil convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EstadoCivil.fromDatabaseValue(dbData);
    }
}


