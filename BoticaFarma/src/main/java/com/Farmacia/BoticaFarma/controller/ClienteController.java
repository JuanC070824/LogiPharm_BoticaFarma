package com.Farmacia.BoticaFarma.controller;

import com.Farmacia.BoticaFarma.dto.ClienteDTO;
import com.Farmacia.BoticaFarma.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/boticafarma/clientes")
@CrossOrigin(origins = "http://localhost:5173")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FARMACEUTICO')")
    public ResponseEntity<Map<String, Object>> listarClientes() {
        try {
            List<ClienteDTO> clientes = clienteService.listarClientes();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("clientes", clientes);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al listar clientes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FARMACEUTICO')")
    public ResponseEntity<Map<String, Object>> obtenerCliente(@PathVariable Integer id) {
        try {
            ClienteDTO cliente = clienteService.obtenerClientePorId(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("cliente", cliente);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FARMACEUTICO')")
    public ResponseEntity<Map<String, Object>> crearCliente(
            @Valid @RequestBody ClienteDTO dto
    ) {
        try {
            ClienteDTO cliente = clienteService.crearCliente(dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cliente creado correctamente");
            response.put("cliente", cliente);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FARMACEUTICO')")
    public ResponseEntity<Map<String, Object>> actualizarCliente(
            @PathVariable Integer id,
            @Valid @RequestBody ClienteDTO dto
    ) {
        try {
            ClienteDTO cliente = clienteService.actualizarCliente(id, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cliente actualizado correctamente");
            response.put("cliente", cliente);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FARMACEUTICO')")
    public ResponseEntity<Map<String, Object>> eliminarCliente(@PathVariable Integer id) {
        try {
            clienteService.eliminarCliente(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cliente eliminado correctamente");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
