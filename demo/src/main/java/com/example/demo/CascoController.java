package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CascoController {

    @GetMapping("/casco")
    public String casco() {
        return "Casco de protección: homologado EN 397, talla M, color amarillo";
    }
}