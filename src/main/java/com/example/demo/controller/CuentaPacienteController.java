package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CuentaPaciente;
import com.example.demo.service.CuentaPacienteService;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaPacienteController {

    private final CuentaPacienteService service;

    public CuentaPacienteController(CuentaPacienteService service) {
        this.service = service;
    }

    // LEER todas: GET /api/cuentas
    @GetMapping
    public List<CuentaPaciente> listar() {
        return service.listarTodas();
    }

    // LEER una: GET /api/cuentas/1
    @GetMapping("/{id}")
    public ResponseEntity<CuentaPaciente> obtener(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREAR: POST /api/cuentas
    @PostMapping
    public ResponseEntity<CuentaPaciente> crear(@RequestBody CuentaPaciente cuenta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(cuenta));
    }

    // ACTUALIZAR: PUT /api/cuentas/1
    @PutMapping("/{id}")
    public ResponseEntity<CuentaPaciente> actualizar(@PathVariable Long id, @RequestBody CuentaPaciente datos) {
        return service.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // BORRAR: DELETE /api/cuentas/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        if (service.borrar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ACTIVAR CUENTA (CU28): POST /api/cuentas/activar
    @PostMapping("/activar")
    public CuentaPaciente activar(@RequestBody SolicitudActivacion solicitud) {
        return service.activar(solicitud.codigo(), solicitud.contrasena(), solicitud.aceptaConsentimiento());
    }

    // Si el Service lanza un error del CU28, se devuelve 400 con el mensaje
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> error(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    public record SolicitudActivacion(String codigo, String contrasena, boolean aceptaConsentimiento) {
    }
}