package com.Farmacia.BoticaFarma.controller;

import com.Farmacia.BoticaFarma.dto.UsuarioDTO;
import com.Farmacia.BoticaFarma.dto.ClienteDTO;
import com.Farmacia.BoticaFarma.service.UsuarioService;
import com.Farmacia.BoticaFarma.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class ReporteController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClienteService clienteService;

    // ============ USUARIOS DEL SISTEMA (Solo ADMIN) ============

    @GetMapping("/lista")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO nuevoUsuario = usuarioService.crear(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @PutMapping("/actualizar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(
            @PathVariable Integer id,
            @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO actualizado = usuarioService.actualizar(id, usuarioDTO);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ============ CLIENTES (ADMIN y FARMACEUTICO) ============

    @GetMapping("/clientes")
    @PreAuthorize("hasAnyRole('ADMIN', 'FARMACEUTICO')")
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        List<ClienteDTO> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }

    @PostMapping("/clientes/crear")
    @PreAuthorize("hasAnyRole('ADMIN', 'FARMACEUTICO')")
    public ResponseEntity<ClienteDTO> crearCliente(@RequestBody ClienteDTO clienteDTO) {
        ClienteDTO nuevoCliente = clienteService.crear(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @PutMapping("/clientes/actualizar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FARMACEUTICO')")
    public ResponseEntity<ClienteDTO> actualizarCliente(
            @PathVariable Integer id,
            @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO actualizado = clienteService.actualizar(id, clienteDTO);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/clientes/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FARMACEUTICO')")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Integer id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}