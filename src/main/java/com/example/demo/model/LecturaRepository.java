package com.example.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturaRepository extends JpaRepository<Lectura, Long> {

    List<Lectura> findByPacienteIdOrderByFechaHoraDesc(String pacienteId);
}