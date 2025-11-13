package com.Farmacia.BoticaFarma.controller;

import com.Farmacia.BoticaFarma.dto.ProveedorDTO;
import com.Farmacia.BoticaFarma.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/boticafarma/proveedores")
@CrossOrigin(origins = "http://localhost:5173")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    // Listar todos los proveedores
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarProveedores() {
        try {
            List<ProveedorDTO> proveedores = proveedorService.listarProveedores();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("proveedores", proveedores);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al listar proveedores: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Obtener proveedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerProveedor(@PathVariable Integer id) {
        try {
            ProveedorDTO proveedor = proveedorService.obtenerProveedorPorId(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("proveedor", proveedor);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Crear proveedor
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearProveedor(
            @Valid @RequestBody ProveedorDTO dto
    ) {
        try {
            ProveedorDTO proveedor = proveedorService.crearProveedor(dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Proveedor creado correctamente");
            response.put("proveedor", proveedor);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Actualizar proveedor
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarProveedor(
            @PathVariable Integer id,
            @Valid @RequestBody ProveedorDTO dto
    ) {
        try {
            ProveedorDTO proveedor = proveedorService.actualizarProveedor(id, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Proveedor actualizado correctamente");
            response.put("proveedor", proveedor);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Eliminar proveedor
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarProveedor(@PathVariable Integer id) {
        try {
            proveedorService.eliminarProveedor(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Proveedor eliminado correctamente");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Buscar proveedores por nombre
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarProveedores(
            @RequestParam String nombre
    ) {
        try {
            List<ProveedorDTO> proveedores = proveedorService.buscarPorNombre(nombre);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("proveedores", proveedores);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al buscar proveedores: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}