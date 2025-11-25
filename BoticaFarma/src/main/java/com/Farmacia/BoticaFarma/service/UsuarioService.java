package com.Farmacia.BoticaFarma.service;

import com.Farmacia.BoticaFarma.dto.UsuarioDTO;
import com.Farmacia.BoticaFarma.model.Usuario;
import com.Farmacia.BoticaFarma.model.Rol;
import com.Farmacia.BoticaFarma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ============ MÉTODOS EXISTENTES (Login/Registro) ============

    // Registrar usuario y encriptar su contraseña
    public Usuario registrarUsuario(Usuario usuario) {
        // ENCRIPTACIÓN DE LA CONTRASEÑA ANTES DE GUARDAR
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    // AUTENTICAR USUARIO
    public Usuario autenticar(String username, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // COMPARO LA CONTRASEÑA INGRESADA CON LA ENCRIPTADA EN LA BD
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                return usuario;
            }
        }
        return null;
    }

    // ============ MÉTODOS NUEVOS (Gestión de Usuarios) ============

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO crear(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApat(dto.getApat());
        usuario.setAmat(dto.getAmat());
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        // Convertir String a ENUM
        usuario.setRol(Rol.valueOf(dto.getRol()));

        Usuario guardado = usuarioRepository.save(usuario);
        return convertirADTO(guardado);
    }

    public UsuarioDTO actualizar(Integer id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(dto.getNombre());
        usuario.setApat(dto.getApat());
        usuario.setAmat(dto.getAmat());
        usuario.setUsername(dto.getUsername());
        // Convertir String a ENUM
        usuario.setRol(Rol.valueOf(dto.getRol()));

        // Solo actualizar contraseña si se proporciona una nueva
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return convertirADTO(actualizado);
    }

    public void eliminar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApat(usuario.getApat());
        dto.setAmat(usuario.getAmat());
        dto.setUsername(usuario.getUsername());
        // Convertir ENUM a String
        dto.setRol(usuario.getRol().name());
        dto.setNombreCompleto(usuario.getNombre() + " " + usuario.getApat() + " " + usuario.getAmat());
        // NO devolver el password
        return dto;
    }
}
