package com.example.demo.model;

/**
 * Color de una lectura de K+ según los umbrales por defecto (apartado 5.3):
 * VERDE de 3,5 a 5,0 mEq/L; AMBAR de 3,0 a 3,49 o de 5,01 a 5,5; ROJO por debajo de 3,0 o por encima de 5,5.
 */
public enum NivelK {
    VERDE, AMBAR, ROJO
}
