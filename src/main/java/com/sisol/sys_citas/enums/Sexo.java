package com.sisol.sys_citas.enums;

public enum Sexo {
    MASCULINO("MASCULINO"),
    FEMENINO("FEMENINO");

    private final String displayName;

    Sexo(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}