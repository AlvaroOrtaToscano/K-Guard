package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Lectura;
import com.example.demo.model.LecturaRepository;
import com.example.demo.model.NivelK;

@Service
public class LecturaService {

    // Umbrales por defecto del documento de K-Guard (mEq/L)
    private static final double ROJO_BAJO = 3.0;
    private static final double VERDE_MIN = 3.5;
    private static final double VERDE_MAX = 5.0;
    private static final double ROJO_ALTO = 5.5;

    private final LecturaRepository repository;

    public LecturaService(LecturaRepository repository) {
        this.repository = repository;
    }

    // LEER: todas las lecturas
    public List<Lectura> listarTodas() {
        return repository.findAll();
    }

    // LEER: una lectura por su id
    public Optional<Lectura> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // LEER: las lecturas de un paciente, de la más reciente a la más antigua
    public List<Lectura> listarPorPaciente(String pacienteId) {
        return repository.findByPacienteIdOrderByFechaHoraDesc(pacienteId);
    }

    // CREAR: llega una lectura nueva del transmisor (CU11)
    public Lectura crear(Lectura lectura) {
        if (lectura.getFechaHora() == null) {
            lectura.setFechaHora(LocalDateTime.now().withNano(0));
        }
        validar(lectura);
        if (repository.existsByTransmisorIdAndFechaHora(lectura.getTransmisorId(), lectura.getFechaHora())) {
            throw new IllegalStateException("La lectura del transmisor " + lectura.getTransmisorId()
                    + " de las " + lectura.getFechaHora() + " ya está guardada.");
        }
        lectura.setId(null);
        lectura.setNivel(clasificar(lectura.getValor()));
        return repository.save(lectura);
    }

    // ACTUALIZAR: corregir una lectura
    public Optional<Lectura> actualizar(Long id, Lectura datos) {
        return repository.findById(id).map(lectura -> {
            if (datos.getFechaHora() == null) {
                datos.setFechaHora(lectura.getFechaHora());
            }
            validar(datos);
            lectura.setPacienteId(datos.getPacienteId());
            lectura.setTransmisorId(datos.getTransmisorId());
            lectura.setValor(datos.getValor());
            lectura.setFechaHora(datos.getFechaHora());
            lectura.setNivel(clasificar(datos.getValor()));
            return repository.save(lectura);
        });
    }

    // BORRAR: eliminar una lectura
    public boolean borrar(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    // Lógica de negocio: la lectura tiene que tener sentido (CU11, flujo 2a)
    private void validar(Lectura lectura) {
        if (lectura.getPacienteId() == null || lectura.getPacienteId().isBlank()) {
            throw new IllegalArgumentException("La lectura tiene que indicar el paciente.");
        }
        if (lectura.getValor() <= 0 || lectura.getValor() > 10) {
            throw new IllegalArgumentException("El valor de potasio no es válido (debe estar entre 0 y 10 mEq/L).");
        }
        if (lectura.getFechaHora().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de la lectura no puede ser futura.");
        }
    }

    // Lógica de negocio: color según los umbrales por defecto (RF2)
    private NivelK clasificar(double valor) {
        if (valor < ROJO_BAJO || valor > ROJO_ALTO) {
            return NivelK.ROJO;
        }
        if (valor < VERDE_MIN || valor > VERDE_MAX) {
            return NivelK.AMBAR;
        }
        return NivelK.VERDE;
    }
}
