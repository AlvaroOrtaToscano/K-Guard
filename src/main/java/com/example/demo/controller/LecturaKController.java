package com.example.demo.controller;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.LecturaK;
import com.example.demo.service.LecturaKService;

/**
 * CRUD de las lecturas de potasio (RF1). Solo usa el servicio, nunca el repositorio.
 *
 *   POST   /lecturas                 -> recibir una lectura (201)
 *   GET    /lecturas[?parche=lote]   -> listar, de la más reciente a la más antigua (200)
 *   GET    /lecturas/{id}            -> una lectura (200 o 404)
 *   PUT    /lecturas/{id}            -> corregir una lectura (200, 400, 404 o 409)
 *   DELETE /lecturas/{id}            -> borrar una lectura (204 o 404)
 */
@RestController
@RequestMapping("/lecturas")
public class LecturaKController {

    private final LecturaKService lecturaKService;

    public LecturaKController(LecturaKService lecturaKService) {
        this.lecturaKService = lecturaKService;
    }

    // C: el transmisor envía una lectura
    @PostMapping
    public ResponseEntity<LecturaK> registrar(@RequestBody LecturaK lectura) {
        LecturaK registrada = lecturaKService.registrar(lectura);
        return ResponseEntity.created(URI.create("/lecturas/" + registrada.getId())).body(registrada);
    }

    // R: todas las lecturas, o solo las de un parche
    @GetMapping
    public List<LecturaK> listar(@RequestParam(name = "parche", required = false) String parche) {
        return lecturaKService.listar(parche);
    }

    // R: una lectura
    @GetMapping("/{id}")
    public LecturaK buscar(@PathVariable("id") Long id) {
        return lecturaKService.buscar(id);
    }

    // U: corregir una lectura
    @PutMapping("/{id}")
    public LecturaK actualizar(@PathVariable("id") Long id, @RequestBody LecturaK lectura) {
        return lecturaKService.actualizar(id, lectura);
    }

    // D: borrar una lectura
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable("id") Long id) {
        lecturaKService.borrar(id);
        return ResponseEntity.noContent().build();
    }

    // Traduce las excepciones del servicio a códigos HTTP
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noEncontrada(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> datosNoValidos(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> duplicada(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
