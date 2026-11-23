package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Lectura de potasio (K+) que el transmisor envía cada 5 minutos (RF1).
 * Corresponde a la clase LecturaK del diagrama de clases. Hibernate crea a partir
 * de ella la tabla LECTURA_K en H2.
 */
@Entity
public class LecturaK {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Número de lote del parche que tomó la medida
    private String numLoteParche;

    // Contador de la trama BLE (apartado 8.4.2): sirve para descartar lecturas duplicadas
    private Integer numSecuencia;

    private LocalDateTime fechaHora;

    // Concentración de K+ en mEq/L (equivalente a mmol/L)
    private Double valor;

    // Lo calcula el servicio con los umbrales; no lo envía el cliente
    @Enumerated(EnumType.STRING)
    private NivelK color;

    public LecturaK() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumLoteParche() {
        return numLoteParche;
    }

    public void setNumLoteParche(String numLoteParche) {
        this.numLoteParche = numLoteParche;
    }

    public Integer getNumSecuencia() {
        return numSecuencia;
    }

    public void setNumSecuencia(Integer numSecuencia) {
        this.numSecuencia = numSecuencia;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public NivelK getColor() {
        return color;
    }

    public void setColor(NivelK color) {
        this.color = color;
    }
}
