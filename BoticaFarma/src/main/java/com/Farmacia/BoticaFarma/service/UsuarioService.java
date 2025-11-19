package com.Farmacia.BoticaFarma.service;

import com.Farmacia.BoticaFarma.dto.CreateUsuarioDTO;
import com.Farmacia.BoticaFarma.dto.UsuarioDTO;
import com.Farmacia.BoticaFarma.model.Usuario;
import com.Farmacia.BoticaFarma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertirADTO(usuario);
    }

    @Transactional
    public UsuarioDTO crearUsuario(CreateUsuarioDTO dto) {
        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con ese nombre de usuario");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApat(dto.getApat());
        usuario.setAmat(dto.getAmat());
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(dto.getRol());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioGuardado);
    }

    @Transactional
    public UsuarioDTO actualizarUsuario(Integer id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent() && !usuario.getIdUsuario().equals(id)) {
            throw new RuntimeException("Ya existe otro usuario con ese nombre de usuario");
        }

        usuario.setNombre(dto.getNombre());
        usuario.setApat(dto.getApat());
        usuario.setAmat(dto.getAmat());
        usuario.setUsername(dto.getUsername());
        usuario.setRol(dto.getRol());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioActualizado);
    }

    @Transactional
    public void eliminarUsuario(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
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

    private UsuarioDTO convertirADTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApat(),
                usuario.getAmat(),
                usuario.getUsername(),
                usuario.getRol()
        );
    }
}
