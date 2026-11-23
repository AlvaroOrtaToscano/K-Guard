package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.demo.model.LecturaK;
import com.example.demo.model.NivelK;
import com.example.demo.repository.LecturaKRepository;

/**
 * Lógica de negocio de la recepción de lecturas de K+ (RF1, CU11).
 * El controlador solo habla con esta clase y esta clase es la única que usa el repositorio.
 */
@Service
public class LecturaKService {

    // Umbrales por defecto en mEq/L (apartado 5.3)
    private static final double VERDE_MIN = 3.5;
    private static final double VERDE_MAX = 5.0;
    private static final double AMBAR_MIN = 3.0;
    private static final double AMBAR_MAX = 5.5;

    // numSecuencia ocupa 2 bytes en la trama BLE (apartado 8.4.2)
    private static final int NUM_SECUENCIA_MAX = 65535;

    private final LecturaKRepository lecturaKRepository;

    // Inyección de dependencias: Spring nos pasa el repositorio
    public LecturaKService(LecturaKRepository lecturaKRepository) {
        this.lecturaKRepository = lecturaKRepository;
    }

    public List<LecturaK> listar(String numLoteParche) {
        if (numLoteParche == null || numLoteParche.isBlank()) {
            return lecturaKRepository.findAllByOrderByFechaHoraDesc();
        }
        return lecturaKRepository.findByNumLoteParcheOrderByFechaHoraDesc(numLoteParche);
    }

    public LecturaK buscar(Long id) {
        return lecturaKRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe ninguna lectura con id " + id));
    }

    // Recepción de una lectura enviada por el transmisor
    public LecturaK registrar(LecturaK lectura) {
        lectura.setId(null); // el id lo asigna la base de datos
        if (lectura.getFechaHora() == null) {
            lectura.setFechaHora(LocalDateTime.now().withNano(0)); // hora de recepción
        }
        validar(lectura);
        comprobarQueNoEstaDuplicada(lectura);
        lectura.setColor(clasificar(lectura.getValor()));
        return lecturaKRepository.save(lectura);
    }

    public LecturaK actualizar(Long id, LecturaK datos) {
        LecturaK lectura = buscar(id);
        if (datos.getFechaHora() == null) {
            datos.setFechaHora(lectura.getFechaHora()); // si no se indica, se conserva
        }
        // Primero se validan los datos nuevos; la lectura guardada solo cambia si todo es correcto
        validar(datos);
        boolean cambiaTrama = !Objects.equals(lectura.getNumLoteParche(), datos.getNumLoteParche())
                || !Objects.equals(lectura.getNumSecuencia(), datos.getNumSecuencia());
        if (cambiaTrama) {
            comprobarQueNoEstaDuplicada(datos);
        }

        lectura.setNumLoteParche(datos.getNumLoteParche());
        lectura.setNumSecuencia(datos.getNumSecuencia());
        lectura.setFechaHora(datos.getFechaHora());
        lectura.setValor(datos.getValor());
        lectura.setColor(clasificar(lectura.getValor()));
        return lecturaKRepository.save(lectura);
    }

    public void borrar(Long id) {
        if (!lecturaKRepository.existsById(id)) {
            throw new NoSuchElementException("No existe ninguna lectura con id " + id);
        }
        lecturaKRepository.deleteById(id);
    }

    // Umbrales.clasificar() del diagrama de clases, con los valores por defecto
    private NivelK clasificar(double valor) {
        if (valor >= VERDE_MIN && valor <= VERDE_MAX) {
            return NivelK.VERDE;
        }
        if (valor >= AMBAR_MIN && valor <= AMBAR_MAX) {
            return NivelK.AMBAR;
        }
        return NivelK.ROJO;
    }

    private void validar(LecturaK lectura) {
        if (lectura.getNumLoteParche() == null || lectura.getNumLoteParche().isBlank()) {
            throw new IllegalArgumentException("El número de lote del parche es obligatorio");
        }
        if (lectura.getNumSecuencia() == null
                || lectura.getNumSecuencia() < 0 || lectura.getNumSecuencia() > NUM_SECUENCIA_MAX) {
            throw new IllegalArgumentException("El número de secuencia es obligatorio y va de 0 a " + NUM_SECUENCIA_MAX);
        }
        if (lectura.getValor() == null || lectura.getValor() <= 0) {
            throw new IllegalArgumentException("El valor de K+ es obligatorio y debe ser mayor que 0 mEq/L");
        }
        if (lectura.getFechaHora().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de la lectura no puede ser futura");
        }
    }

    // Una trama repetida (mismo parche y número de secuencia) se descarta (apartado 8.4.2)
    private void comprobarQueNoEstaDuplicada(LecturaK lectura) {
        if (lecturaKRepository.existsByNumLoteParcheAndNumSecuencia(
                lectura.getNumLoteParche(), lectura.getNumSecuencia())) {
            throw new IllegalStateException("La lectura " + lectura.getNumSecuencia()
                    + " del parche " + lectura.getNumLoteParche() + " ya está registrada");
        }
    }
}
