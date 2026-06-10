package com.Farmacia.BoticaFarma.controller;


import com.Farmacia.BoticaFarma.config.JwtUtil;
import com.Farmacia.BoticaFarma.model.Usuario;
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

    //Endpoint para registro usuarios
    @PostMapping("/registrarse")
    public ResponseEntity<String> registrar(@RequestBody Map<String,String> request){
        try{
            String nombre=request.get("nombre");
            String apat=request.get("apat");
            String amat=request.get("amat");
            String username=request.get("username");
            String password=request.get("password");
            String rolStr=request.get("rol"); //ADMIN por ejm

            com.Farmacia.BoticaFarma.model.Rol rol= com.Farmacia.BoticaFarma.model.Rol.valueOf(rolStr.toUpperCase());

            com.Farmacia.BoticaFarma.model.Usuario nuevo = new com.Farmacia.BoticaFarma.model.Usuario(
                    nombre, apat, amat, username, password, rol
            );

            usuarioService.registrarUsuario(nuevo);
            return ResponseEntity.ok("Usuario registrado correctamente");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar usuario");
        }
    }
}
