package com.Farmacia.BoticaFarma.controller;

import com.Farmacia.BoticaFarma.dto.CreateProductoDTO;
import com.Farmacia.BoticaFarma.dto.ProductoDTO;
import com.Farmacia.BoticaFarma.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/boticafarma/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Listar productos con paginación filtrados por Botica
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarProductos(
            @RequestAttribute("idBotica") Integer idBotica,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idProducto") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        try {
            Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

            Page<ProductoDTO> productos = productoService.listarProductos(idBotica, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("productos", productos.getContent());
            response.put("currentPage", productos.getNumber());
            response.put("totalItems", productos.getTotalElements());
            response.put("totalPages", productos.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al listar productos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Buscar productos con filtros y Botica
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarProductos(
            @RequestAttribute("idBotica") Integer idBotica,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer idCategoria,
            @RequestParam(required = false) Integer idMarca,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductoDTO> productos = productoService.buscarProductos(
                    idBotica, nombre, idCategoria, idMarca, pageable
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("productos", productos.getContent());
            response.put("currentPage", productos.getNumber());
            response.put("totalItems", productos.getTotalElements());
            response.put("totalPages", productos.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al buscar productos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerProducto(@PathVariable Integer id) {
        try {
            ProductoDTO producto = productoService.obtenerProductoPorId(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("producto", producto);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Crear producto para la Botica del usuario autenticado
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearProducto(
            @RequestAttribute("idBotica") Integer idBotica,
            @Valid @RequestBody CreateProductoDTO dto
    ) {
        try {
            ProductoDTO producto = productoService.crearProducto(idBotica, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto creado correctamente");
            response.put("producto", producto);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarProducto(
            @PathVariable Integer id,
            @Valid @RequestBody CreateProductoDTO dto
    ) {
        try {
            ProductoDTO producto = productoService.actualizarProducto(id, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto actualizado correctamente");
            response.put("producto", producto);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarProducto(@PathVariable Integer id) {
        try {
            productoService.eliminarProducto(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto eliminado correctamente");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Productos con stock bajo por Botica
    @GetMapping("/stock-bajo")
    public ResponseEntity<Map<String, Object>> productosStockBajo(
            @RequestAttribute("idBotica") Integer idBotica,
            @RequestParam(defaultValue = "10") Integer minStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductoDTO> productos = productoService.productosStockBajo(idBotica, minStock, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("productos", productos.getContent());
            response.put("currentPage", productos.getNumber());
            response.put("totalItems", productos.getTotalElements());
            response.put("totalPages", productos.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener productos con stock bajo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}