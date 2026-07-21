package com.Farmacia.BoticaFarma.controller;

import com.Farmacia.BoticaFarma.config.JwtUtil;
import com.Farmacia.BoticaFarma.model.Almacen;
import com.Farmacia.BoticaFarma.model.Botica;
import com.Farmacia.BoticaFarma.model.Rol;
import com.Farmacia.BoticaFarma.model.Usuario;
import com.Farmacia.BoticaFarma.repository.AlmacenRepository;
import com.Farmacia.BoticaFarma.repository.BoticaRepository;
import com.Farmacia.BoticaFarma.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/boticafarma")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BoticaRepository boticaRepository;

    @Autowired
    private AlmacenRepository almacenRepository;

    // Endpoint para el login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Usuario usuario = usuarioService.autenticar(username, password);
        if (usuario != null) {
            String token = jwtUtil.generateToken(usuario);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario autenticado!");
            response.put("token", token);
            response.put("rol", usuario.getRol().toString());
            response.put("nombre", usuario.getNombre());
            response.put("username", usuario.getUsername());

            // Devuelve el contexto multi-tenant al cliente React
            if (usuario.getBotica() != null) {
                response.put("idBotica", usuario.getBotica().getIdBotica());
            }
            if (usuario.getAlmacen() != null) {
                response.put("idAlmacen", usuario.getAlmacen().getIdAlmacen());
            }

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Usuario o contraseña incorrectos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    // Endpoint para registrar empleados/usuarios adicionales dentro de la misma Botica
    @PostMapping("/registrarse")
    public ResponseEntity<String> registrar(
            @RequestAttribute("idBotica") Integer idBotica,
            @RequestAttribute("idAlmacen") Integer idAlmacen,
            @RequestBody Map<String, String> request
    ) {
        try {
            String nombre = request.get("nombre");
            String apat = request.get("apat");
            String amat = request.get("amat");
            String username = request.get("username");
            String password = request.get("password");
            String rolStr = request.get("rol");

            Rol rol = Rol.valueOf(rolStr.toUpperCase());

            Botica botica = boticaRepository.findById(idBotica)
                    .orElseThrow(() -> new RuntimeException("Botica no encontrada"));

            Almacen almacen = almacenRepository.findById(idAlmacen)
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

            // Construcción usando setters para evitar inconsistencias de firmas de constructor
            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setApat(apat);
            nuevo.setAmat(amat);
            nuevo.setUsername(username);
            nuevo.setPassword(password); // El service se encarga del encode si corresponde
            nuevo.setRol(rol);
            nuevo.setBotica(botica);
            nuevo.setAlmacen(almacen);

            usuarioService.registrarUsuario(nuevo);
            return ResponseEntity.ok("Usuario registrado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar usuario: " + e.getMessage());
        }
    }
}