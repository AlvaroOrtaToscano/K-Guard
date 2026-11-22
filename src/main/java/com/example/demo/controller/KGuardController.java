package com.example.demo.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KGuardController {

    @GetMapping("/kguard")
    public Map<String, Object> infoSensor() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("dispositivo", "K-Guard");
        info.put("descripcion", "Sensor de monitorización continua de potasio en sangre");
        info.put("unidad", "mmol/L");
        info.put("modelo", "KG-Patch v1");
        info.put("firmware", "1.0.3");
        info.put("bateriaTransmisor", 87);       // %
        info.put("conexionBLE", "Conectado");
        info.put("diasRestantesParche", 9);
        return info;
    }
}