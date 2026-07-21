package com.Farmacia.BoticaFarma.service;

import com.Farmacia.BoticaFarma.dto.RegistroBoticaDTO;
import com.Farmacia.BoticaFarma.model.Almacen;
import com.Farmacia.BoticaFarma.model.Botica;
import com.Farmacia.BoticaFarma.model.Rol;
import com.Farmacia.BoticaFarma.model.Usuario;
import com.Farmacia.BoticaFarma.repository.AlmacenRepository;
import com.Farmacia.BoticaFarma.repository.BoticaRepository;
import com.Farmacia.BoticaFarma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private BoticaRepository boticaRepository;

    @Autowired
    private AlmacenRepository almacenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Lista de dominios de correo temporal restringidos
    private static final Set<String> DISPOSABLE_EMAIL_DOMAINS = Set.of(
            "tempmail.com", "guerrillamail.com", "10minutemail.com",
            "mailinator.com", "yopmail.com", "trashmail.com", "dispostable.com"
    );

    @Transactional
    public void registrarNuevaBotica(RegistroBoticaDTO dto) {

        // 1. Validar que el correo no pertenezca a un proveedor de tempmail
        if (dto.getCorreo() != null && dto.getCorreo().contains("@")) {
            String domain = dto.getCorreo().substring(dto.getCorreo().indexOf("@") + 1).toLowerCase().trim();
            if (DISPOSABLE_EMAIL_DOMAINS.contains(domain)) {
                throw new IllegalArgumentException("No se permite el registro con correos temporales o desechables.");
            }
        }

        // 2. Validar que el username no esté en uso
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario '" + dto.getUsername() + "' ya se encuentra registrado.");
        }

        // 3. Crear y guardar la entidad matriz (Botica)
        Botica botica = new Botica();
        botica.setNombreBotica(dto.getNombreBotica());
        botica.setRuc(dto.getRuc());
        botica = boticaRepository.save(botica);

        // 4. Crear y guardar la primera Sede/Sucursal (Almacen)
        Almacen almacen = new Almacen();
        almacen.setNombreSucursal(dto.getNombreSede() != null && !dto.getNombreSede().isBlank()
                ? dto.getNombreSede() : "Sede Central");
        almacen.setDireccion(dto.getDireccionLocal());
        almacen.setBotica(botica);

        // Asignación de valores por defecto para evitar nulos
        almacen.setLatitud(BigDecimal.ZERO);
        almacen.setLongitud(BigDecimal.ZERO);

        almacen = almacenRepository.save(almacen);

        // 5. Crear y guardar el Usuario Administrador principal
        Usuario admin = new Usuario();

        // Asignación con fallback por si acaso algún campo llega nulo
        admin.setNombre(dto.getNombreAdmin() != null ? dto.getNombreAdmin() : "Admin");
        admin.setApat(dto.getApatAdmin() != null ? dto.getApatAdmin() : "Sin Apellido");
        admin.setAmat(dto.getAmatAdmin() != null ? dto.getAmatAdmin() : "Sin Apellido");

        admin.setUsername(dto.getUsername());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setEmail(dto.getCorreo());
        admin.setRol(Rol.ADMIN);
        admin.setBotica(botica);
        admin.setAlmacen(almacen);

        usuarioRepository.save(admin);
    }
}