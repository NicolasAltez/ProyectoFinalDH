package com.integrador.servicios_tecnicos.enums;

public enum ProductType {
    PLOMERIA,
    ELECTRICIDAD,
    PINTURERIA,
    CARPINTERIA;

    public String getDescripcion() {
        return switch (this) {
            case PLOMERIA -> "Servicios de plomería";
            case ELECTRICIDAD -> "Servicios eléctricos";
            case PINTURERIA -> "Servicios de pintura";
            case CARPINTERIA -> "Servicios de carpintería";
        };
    }
}
