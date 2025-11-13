package com.Farmacia.BoticaFarma.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Farmacia.BoticaFarma.model.Usuario;
import com.Farmacia.BoticaFarma.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //Registrar usuario y encriptar su contraseña
    public Usuario registrarUsuario(Usuario usuario){
        //ENCRIPTACION DE LA CONTRASEÑA ANTES DE GUARDAR
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    //AUTENTICAR USUARIO
    public Usuario autenticar(String username,String password){
        Optional<Usuario> usuarioOpt=usuarioRepository.findByUsername(username);
        if(usuarioOpt.isPresent()){
            Usuario usuario=usuarioOpt.get();
            //COMPARO LA CONTRASEÑA INGRESADA CON LA ENCRIPTADA EN LA BD
            if(passwordEncoder.matches(password,usuario.getPassword())){
                return usuario;
            }
        }
        return null;
    }






}
