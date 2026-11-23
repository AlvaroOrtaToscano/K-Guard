package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Lectura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pacienteId;
    private String transmisorId;
    private double valor;
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    private NivelK nivel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPacienteId() { return pacienteId; }
    public void setPacienteId(String pacienteId) { this.pacienteId = pacienteId; }

    public String getTransmisorId() { return transmisorId; }
    public void setTransmisorId(String transmisorId) { this.transmisorId = transmisorId; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public NivelK getNivel() { return nivel; }
    public void setNivel(NivelK nivel) { this.nivel = nivel; }
}