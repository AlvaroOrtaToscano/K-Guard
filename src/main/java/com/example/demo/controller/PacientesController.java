package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de K-Guard que muestra los pacientes monitorizados
 * y el parche de microagujas que lleva asignado cada uno.
 *
 * Por ahora los datos están simulados; más adelante vendrán de la base de datos.
 *
 * Endpoint: GET http://localhost:8080/pacientes
 */
@RestController
public class PacientesController {

    @GetMapping("/pacientes")
    public List<Paciente> pacientesMonitorizados() {
        return List.of(
                new Paciente("PAC-001", "Carmen Ruiz López", 72, "Insuficiencia cardíaca",
                        "Hospital Regional Universitario de Málaga", "KG-0427"),
                new Paciente("PAC-002", "Antonio García Molina", 65, "Enfermedad renal crónica (estadio 4)",
                        "Hospital Regional Universitario de Málaga", "KG-0513"),
                new Paciente("PAC-003", "Dolores Martín Vega", 81, "Insuficiencia cardíaca",
                        "Hospital Universitario Virgen de la Victoria", "KG-0601"),
                new Paciente("PAC-004", "José Fernández Ortiz", 58, "Enfermedad renal crónica (estadio 5, en diálisis)",
                        "Hospital Universitario Virgen de la Victoria", "KG-0718"));
    }

    public record Paciente(
            String id,
            String nombre,
            int edad,
            String diagnostico,
            String hospital,
            String parcheAsignado) {
    }
}