package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Lectura;
import com.example.demo.model.LecturaRepository;

@Service
public class LecturaService {

    // Rangos de K+ en sangre (mEq/L) del documento de K-Guard
    private static final double CRITICO_BAJO = 2.5;
    private static final double LIMITE_INFERIOR = 3.5;
    private static final double LIMITE_SUPERIOR = 5.0;
    private static final double CRITICO_ALTO = 6.0;

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

    // CREAR: llega una lectura nueva del transmisor
    public Lectura crear(Lectura lectura) {
        validar(lectura);
        lectura.setId(null);
        if (lectura.getFechaHora() == null) {
            lectura.setFechaHora(LocalDateTime.now().withNano(0));
        }
        lectura.setEstado(clasificar(lectura.getValor()));
        return repository.save(lectura);
    }

    // ACTUALIZAR: corregir una lectura
    public Optional<Lectura> actualizar(Long id, Lectura datos) {
        validar(datos);
        return repository.findById(id).map(lectura -> {
            lectura.setPacienteId(datos.getPacienteId());
            lectura.setTransmisorId(datos.getTransmisorId());
            lectura.setValor(datos.getValor());
            if (datos.getFechaHora() != null) {
                lectura.setFechaHora(datos.getFechaHora());
            }
            lectura.setEstado(clasificar(datos.getValor()));
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

    // Lógica de negocio: comprobar que la lectura tiene sentido
    private void validar(Lectura lectura) {
        if (lectura.getPacienteId() == null || lectura.getPacienteId().isBlank()) {
            throw new IllegalArgumentException("La lectura tiene que indicar el paciente.");
        }
        if (lectura.getValor() <= 0 || lectura.getValor() > 10) {
            throw new IllegalArgumentException("El valor de potasio no es válido (debe estar entre 0 y 10 mEq/L).");
        }
    }

    // Lógica de negocio: clasificar el potasio según los rangos
    private String clasificar(double valor) {
        if (valor < CRITICO_BAJO || valor > CRITICO_ALTO) {
            return "CRITICO";
        }
        if (valor < LIMITE_INFERIOR) {
            return "HIPOPOTASEMIA";
        }
        if (valor > LIMITE_SUPERIOR) {
            return "HIPERPOTASEMIA";
        }
        return "NORMAL";
    }
}