package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CuentaPaciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombrePaciente;
    private String codigoActivacion;
    private String contrasena;
    private boolean consentimientoAceptado;
    private LocalDate fechaConsentimiento;
    private String idioma;
    private String tamanoLetra;
    private boolean activa;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public String getCodigoActivacion() { return codigoActivacion; }
    public void setCodigoActivacion(String codigoActivacion) { this.codigoActivacion = codigoActivacion; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public boolean isConsentimientoAceptado() { return consentimientoAceptado; }
    public void setConsentimientoAceptado(boolean consentimientoAceptado) { this.consentimientoAceptado = consentimientoAceptado; }

    public LocalDate getFechaConsentimiento() { return fechaConsentimiento; }
    public void setFechaConsentimiento(LocalDate fechaConsentimiento) { this.fechaConsentimiento = fechaConsentimiento; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public String getTamanoLetra() { return tamanoLetra; }
    public void setTamanoLetra(String tamanoLetra) { this.tamanoLetra = tamanoLetra; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}