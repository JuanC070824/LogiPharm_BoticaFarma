package com.Farmacia.BoticaFarma.controller;

import com.Farmacia.BoticaFarma.dto.CategoriaDTO;
import com.Farmacia.BoticaFarma.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/boticafarma/categorias")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Listar todas las categorías
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarCategorias() {
        try {
            List<CategoriaDTO> categorias = categoriaService.listarCategorias();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("categorias", categorias);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al listar categorías: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Obtener categoría por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerCategoria(@PathVariable Integer id) {
        try {
            CategoriaDTO categoria = categoriaService.obtenerCategoriaPorId(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("categoria", categoria);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Crear categoría
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearCategoria(
            @Valid @RequestBody CategoriaDTO dto
    ) {
        try {
            CategoriaDTO categoria = categoriaService.crearCategoria(dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Categoría creada correctamente");
            response.put("categoria", categoria);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Actualizar categoría
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarCategoria(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaDTO dto
    ) {
        try {
            CategoriaDTO categoria = categoriaService.actualizarCategoria(id, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Categoría actualizada correctamente");
            response.put("categoria", categoria);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Eliminar categoría
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarCategoria(@PathVariable Integer id) {
        try {
            categoriaService.eliminarCategoria(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Categoría eliminada correctamente");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}