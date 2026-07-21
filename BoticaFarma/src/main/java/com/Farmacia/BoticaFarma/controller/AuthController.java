package com.Farmacia.BoticaFarma.controller;

import com.Farmacia.BoticaFarma.dto.RegistroBoticaDTO;
import com.Farmacia.BoticaFarma.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register-botica")
    public ResponseEntity<?> registrarBotica(@RequestBody RegistroBoticaDTO dto) {
        try {
            authService.registrarNuevaBotica(dto);
            return ResponseEntity.ok(Map.of("message", "Botica y usuario administrador registrados con éxito"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Error interno al registrar botica: " + e.getMessage()));
        }
    }
}