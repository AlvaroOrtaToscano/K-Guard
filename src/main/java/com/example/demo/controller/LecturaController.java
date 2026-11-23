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

import com.example.demo.model.Lectura;
import com.example.demo.service.LecturaService;

@RestController
@RequestMapping("/api/lecturas")
public class LecturaController {

    private final LecturaService service;

    public LecturaController(LecturaService service) {
        this.service = service;
    }

    // LEER todas: GET /api/lecturas
    @GetMapping
    public List<Lectura> listar() {
        return service.listarTodas();
    }

    // LEER una: GET /api/lecturas/1
    @GetMapping("/{id}")
    public ResponseEntity<Lectura> obtener(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // LEER las de un paciente: GET /api/lecturas/paciente/PAC-001
    @GetMapping("/paciente/{pacienteId}")
    public List<Lectura> listarPorPaciente(@PathVariable String pacienteId) {
        return service.listarPorPaciente(pacienteId);
    }

    // CREAR: POST /api/lecturas
    @PostMapping
    public ResponseEntity<Lectura> crear(@RequestBody Lectura lectura) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(lectura));
    }

    // ACTUALIZAR: PUT /api/lecturas/1
    @PutMapping("/{id}")
    public ResponseEntity<Lectura> actualizar(@PathVariable Long id, @RequestBody Lectura datos) {
        return service.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // BORRAR: DELETE /api/lecturas/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        if (service.borrar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Si el Service rechaza la lectura, se devuelve 400 con el mensaje
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> error(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    // Si la lectura ya estaba guardada (repetida), se devuelve 409
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> repetida(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}