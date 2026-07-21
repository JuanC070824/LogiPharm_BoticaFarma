package com.Farmacia.BoticaFarma.service;

import com.Farmacia.BoticaFarma.dto.UsuarioDTO;
import com.Farmacia.BoticaFarma.model.Usuario;
import com.Farmacia.BoticaFarma.model.Rol;
import com.Farmacia.BoticaFarma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.Farmacia.BoticaFarma.model.Botica;
import com.Farmacia.BoticaFarma.model.Almacen;
import com.Farmacia.BoticaFarma.repository.BoticaRepository;
import com.Farmacia.BoticaFarma.repository.AlmacenRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private BoticaRepository boticaRepository;

    @Autowired
    private AlmacenRepository almacenRepository;

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

    public UsuarioDTO crear(Integer idBotica, UsuarioDTO dto) { // ============ CAMBIO: se agrega parámetro idBotica ============
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApat(dto.getApat());
        usuario.setAmat(dto.getAmat());

        // ============ AÑADIR: asignar la Botica (siempre la del ADMIN logueado) ============
        Botica botica = boticaRepository.findById(idBotica)
                .orElseThrow(() -> new RuntimeException("Botica no encontrada"));
        usuario.setBotica(botica);
        // ============ FIN ============

        // ============ AÑADIR: asignar el Almacen/sucursal elegida en el formulario ============
        if (dto.getIdAlmacen() != null) {
            Almacen almacen = almacenRepository.findById(dto.getIdAlmacen())
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));
            usuario.setAlmacen(almacen);
        }
        // ============ FIN ============

        Rol rol = Rol.valueOf(dto.getRol());
        if (rol == Rol.REPARTIDOR) {
            String usernameAuto = "REP_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            usuario.setUsername(usernameAuto);

            String passwordDummy = UUID.randomUUID().toString();
            usuario.setPassword(passwordEncoder.encode(passwordDummy));
        } else {
            usuario.setUsername(dto.getUsername());
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

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

        Rol rol = Rol.valueOf(dto.getRol());

        if(rol==Rol.REPARTIDOR){
            //No se puede cambiar el username de un repartidor
        }else{
            usuario.setUsername(dto.getUsername());


            // Solo actualizar contraseña si se proporciona una nueva
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
        }

        usuario.setRol(Rol.valueOf(dto.getRol()));
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
        dto.setRol(usuario.getRol().name());
        dto.setNombreCompleto(usuario.getNombre() + " " + usuario.getApat() + " " + usuario.getAmat());

        // ============ AÑADIR: incluir idAlmacen para poder filtrar repartidores por sucursal en el frontend ============
        if (usuario.getAlmacen() != null) {
            dto.setIdAlmacen(usuario.getAlmacen().getIdAlmacen());
        }
        // ============ FIN ============

        return dto;
    }
}
