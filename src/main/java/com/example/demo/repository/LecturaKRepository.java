package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.LecturaK;

/**
 * Acceso a la tabla LECTURAK. JpaRepository ya da save, findAll, findById,
 * existsById y deleteById; los métodos de abajo los genera Spring a partir del nombre.
 */
public interface LecturaKRepository extends JpaRepository<LecturaK, Long> {

    // Todas las lecturas, de la más reciente a la más antigua
    List<LecturaK> findAllByOrderByFechaHoraDesc();

    // Lecturas de un parche, de la más reciente a la más antigua
    List<LecturaK> findByNumLoteParcheOrderByFechaHoraDesc(String numLoteParche);

    // ¿Ya se recibió esta trama de este parche?
    boolean existsByNumLoteParcheAndNumSecuencia(String numLoteParche, Integer numSecuencia);
}
