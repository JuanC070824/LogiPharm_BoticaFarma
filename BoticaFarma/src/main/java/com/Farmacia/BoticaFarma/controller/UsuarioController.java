package com.Farmacia.BoticaFarma.controller;

import com.Farmacia.BoticaFarma.config.JwtUtil;
import com.Farmacia.BoticaFarma.dto.CreateUsuarioDTO;
import com.Farmacia.BoticaFarma.dto.UsuarioDTO;
import com.Farmacia.BoticaFarma.model.Usuario;
import com.Farmacia.BoticaFarma.service.UsuarioService;
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
@RequestMapping("/boticafarma")
@CrossOrigin(origins = "http://localhost:5173") // permite que React acceda, antes era 3000
public class UsuarioController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioService usuarioService;

    //Endpoint para el login
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody Map<String,String> request){
        String username = request.get("username");
        String password = request.get("password");

        Usuario usuario = usuarioService.autenticar(username, password);
        if(usuario!=null){
            String token = jwtUtil.generateToken(usuario);
            Map<String,Object> response = new HashMap<>();
            response.put("success",true);
            response.put("message","Usuario autenticado!");
            response.put("token",token);
            response.put("rol",usuario.getRol().toString());
            response.put("nombre",usuario.getNombre());
            response.put("username",usuario.getUsername());

            return ResponseEntity.ok(response);
        }else{
            Map<String,Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message","Usuario o contraseña incorrectos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/usuarios")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> listarUsuarios() {
        try {
            List<UsuarioDTO> usuarios = usuarioService.listarUsuarios();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("usuarios", usuarios);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al listar usuarios: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/usuarios/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> obtenerUsuario(@PathVariable Integer id) {
        try {
            UsuarioDTO usuario = usuarioService.obtenerUsuarioPorId(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("usuario", usuario);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping("/usuarios")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> crearUsuario(@Valid @RequestBody CreateUsuarioDTO dto) {
        try {
            UsuarioDTO usuario = usuarioService.crearUsuario(dto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario creado correctamente");
            response.put("usuario", usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/usuarios/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> actualizarUsuario(@PathVariable Integer id, @Valid @RequestBody UsuarioDTO dto) {
        try {
            UsuarioDTO usuario = usuarioService.actualizarUsuario(id, dto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario actualizado correctamente");
            response.put("usuario", usuario);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/usuarios/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> eliminarUsuario(@PathVariable Integer id) {
        try {
            usuarioService.eliminarUsuario(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario eliminado correctamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
