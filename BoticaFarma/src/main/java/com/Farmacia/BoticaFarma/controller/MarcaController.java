package com.Farmacia.BoticaFarma.controller;

import com.Farmacia.BoticaFarma.dto.MarcaDTO;
import com.Farmacia.BoticaFarma.service.MarcaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/boticafarma/marcas")
@CrossOrigin(origins = "http://localhost:5173")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    // Listar todas las marcas
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarMarcas() {
        try {
            List<MarcaDTO> marcas = marcaService.listarMarcas();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("marcas", marcas);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al listar marcas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Obtener marca por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerMarca(@PathVariable Integer id) {
        try {
            MarcaDTO marca = marcaService.obtenerMarcaPorId(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("marca", marca);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Listar marcas por proveedor
    @GetMapping("/proveedor/{idProveedor}")
    public ResponseEntity<Map<String, Object>> listarMarcasPorProveedor(
            @PathVariable Integer idProveedor
    ) {
        try {
            List<MarcaDTO> marcas = marcaService.listarMarcasPorProveedor(idProveedor);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("marcas", marcas);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al listar marcas por proveedor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Crear marca
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearMarca(
            @Valid @RequestBody MarcaDTO dto
    ) {
        try {
            MarcaDTO marca = marcaService.crearMarca(dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Marca creada correctamente");
            response.put("marca", marca);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Actualizar marca
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarMarca(
            @PathVariable Integer id,
            @Valid @RequestBody MarcaDTO dto
    ) {
        try {
            MarcaDTO marca = marcaService.actualizarMarca(id, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Marca actualizada correctamente");
            response.put("marca", marca);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Eliminar marca
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarMarca(@PathVariable Integer id) {
        try {
            marcaService.eliminarMarca(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Marca eliminada correctamente");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}