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
        info.put("rangoNormalMin", 3.5);
        info.put("rangoNormalMax", 5.0);
        info.put("ultimaLectura", 4.2);
        info.put("estado", "Normal");
        return info;
    }
}