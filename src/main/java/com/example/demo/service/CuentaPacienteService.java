package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.CuentaPaciente;
import com.example.demo.model.CuentaPacienteRepository;

@Service
public class CuentaPacienteService {

    private final CuentaPacienteRepository repository;

    public CuentaPacienteService(CuentaPacienteRepository repository) {
        this.repository = repository;
    }

    // LEER: todas las cuentas
    public List<CuentaPaciente> listarTodas() {
        return repository.findAll();
    }

    // LEER: una cuenta por su id
    public Optional<CuentaPaciente> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // CREAR: el hospital da de alta la cuenta (todavía sin activar)
    public CuentaPaciente crear(CuentaPaciente cuenta) {
        cuenta.setId(null);
        cuenta.setActiva(false);
        cuenta.setConsentimientoAceptado(false);
        cuenta.setFechaConsentimiento(null);
        cuenta.setContrasena(null);
        if (cuenta.getIdioma() == null) {
            cuenta.setIdioma("es");
        }
        if (cuenta.getTamanoLetra() == null) {
            cuenta.setTamanoLetra("NORMAL");
        }
        return repository.save(cuenta);
    }

    // ACTUALIZAR: el paciente configura su perfil
    public Optional<CuentaPaciente> actualizar(Long id, CuentaPaciente datos) {
        return repository.findById(id).map(cuenta -> {
            cuenta.setNombrePaciente(datos.getNombrePaciente());
            cuenta.setIdioma(datos.getIdioma());
            cuenta.setTamanoLetra(datos.getTamanoLetra());
            return repository.save(cuenta);
        });
    }

    // BORRAR: dar de baja la cuenta
    public boolean borrar(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    // ACTIVAR (CU28): reglas del RF20
    public CuentaPaciente activar(String codigo, String contrasena, boolean aceptaConsentimiento) {
        CuentaPaciente cuenta = repository.findByCodigoActivacion(codigo)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El código no es correcto. Contacte con el hospital."));

        if (cuenta.isActiva()) {
            throw new IllegalArgumentException("La cuenta ya está activada.");
        }
        if (!aceptaConsentimiento) {
            throw new IllegalArgumentException(
                    "Sin aceptar el consentimiento informado la cuenta no se puede activar.");
        }
        if (contrasena == null || contrasena.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }

        cuenta.setContrasena(contrasena);
        cuenta.setConsentimientoAceptado(true);
        cuenta.setFechaConsentimiento(LocalDate.now());
        cuenta.setActiva(true);
        return repository.save(cuenta);
    }
}