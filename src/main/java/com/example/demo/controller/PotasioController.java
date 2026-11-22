package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controlador de K-Guard que muestra las últimas lecturas de potasio (K+)
 * enviadas por los parches de microagujas de los pacientes monitorizados.
 *
 * Por ahora los datos están simulados; más adelante vendrán de la base de datos.
 *
 * Endpoints:
 *   GET http://localhost:8080/potasio              -> lecturas de todos los pacientes
 *   GET http://localhost:8080/potasio/{pacienteId} -> lectura de un solo paciente (404 si no existe)
 */
@RestController
public class PotasioController {

	// Rangos de referencia del potasio en sangre (mmol/L)
	private static final double LIMITE_INFERIOR = 3.5;
	private static final double LIMITE_SUPERIOR = 5.0;
	private static final double LIMITE_CRITICO = 6.0;

	@GetMapping("/potasio")
	public List<LecturaPotasio> ultimasLecturas() {
		LocalDateTime ahora = LocalDateTime.now().withNano(0);
		return List.of(
				crearLectura("PAC-001", "KG-0427", 4.2, ahora),
				crearLectura("PAC-002", "KG-0513", 5.6, ahora.minusMinutes(2)),
				crearLectura("PAC-003", "KG-0601", 3.1, ahora.minusMinutes(5)),
				crearLectura("PAC-004", "KG-0718", 6.4, ahora.minusMinutes(8)));
	}

	@GetMapping("/potasio/{pacienteId}")
	public LecturaPotasio lecturaPaciente(@PathVariable("pacienteId") String pacienteId) {
		return ultimasLecturas().stream()
				.filter(lectura -> lectura.pacienteId().equalsIgnoreCase(pacienteId))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"No existe ningún paciente con id " + pacienteId));
	}

	private LecturaPotasio crearLectura(String pacienteId, String parcheId, double valor, LocalDateTime fechaHora) {
		EstadoPotasio estado = clasificar(valor);
		return new LecturaPotasio(pacienteId, parcheId, valor, "mmol/L", estado, estado != EstadoPotasio.NORMAL, fechaHora);
	}

	private EstadoPotasio clasificar(double valor) {
		if (valor < LIMITE_INFERIOR) {
			return EstadoPotasio.HIPOPOTASEMIA;
		}
		if (valor <= LIMITE_SUPERIOR) {
			return EstadoPotasio.NORMAL;
		}
		if (valor < LIMITE_CRITICO) {
			return EstadoPotasio.HIPERPOTASEMIA;
		}
		return EstadoPotasio.CRITICO;
	}

	public enum EstadoPotasio {
		HIPOPOTASEMIA, NORMAL, HIPERPOTASEMIA, CRITICO
	}

	public record LecturaPotasio(
			String pacienteId,
			String parcheId,
			double valor,
			String unidad,
			EstadoPotasio estado,
			boolean alerta,
			LocalDateTime fechaHora) {
	}

}
