package com.sisol.sys_citas.enums;

public enum EstadoCivil {
    SOLTERO("SOLTERO"),
    CASADO("CASADO"),
    DIVORCIADO("DIVORCIADO"),
    VIUDO("VIUDO"),
    UNION_LIBRE("UNION_LIBRE");

    private final String displayName;

    EstadoCivil(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    public String getValue() { return displayName; }

    public static EstadoCivil fromDatabaseValue(String value) {
        // Normalizar el valor removiendo "/A" si existe
        String normalizedValue = value;
        if (value != null && value.endsWith("/A")) {
            normalizedValue = value.substring(0, value.length() - 2);
        }
        
        for (EstadoCivil ec : values()) {
            if (ec.displayName.equalsIgnoreCase(normalizedValue)) {
                return ec;
            }
        }
        throw new IllegalArgumentException("EstadoCivil inválido: " + value);
    }
}