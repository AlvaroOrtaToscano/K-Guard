package com.example.demo.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaPacienteRepository extends JpaRepository<CuentaPaciente, Long> {

    Optional<CuentaPaciente> findByCodigoActivacion(String codigoActivacion);
}