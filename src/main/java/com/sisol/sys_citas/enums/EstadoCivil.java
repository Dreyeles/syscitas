package com.sisol.sys_citas.enums;

public enum EstadoCivil {
    SOLTERO("Soltero(a)"),
    CASADO("Casado(a)"),
    DIVORCIADO("Divorciado(a)"),
    VIUDO("Viudo(a)"),
    UNION_LIBRE("Unión Libre");

    private final String displayName;

    EstadoCivil(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}