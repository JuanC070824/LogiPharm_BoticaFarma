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

@Service
public class RegistroService {

    @Autowired
    private BoticaRepository boticaRepository;

    @Autowired
    private AlmacenRepository almacenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void registrarNuevaBotica(RegistroBoticaDTO dto) {

        // 1. Guardar Botica
        Botica botica = new Botica();
        botica.setNombreBotica(dto.getNombreBotica());
        botica.setRuc(dto.getRuc());
        botica = boticaRepository.save(botica);

        // 2. Guardar Sede Central (Almacen)
        Almacen almacen = new Almacen();
        almacen.setNombreSucursal(dto.getNombreSede() != null && !dto.getNombreSede().isBlank()
                ? dto.getNombreSede() : "Sede Central");
        almacen.setDireccion(dto.getDireccionLocal());
        almacen.setBotica(botica);

        // Asignación de valores por defecto para evitar 'Column latitud cannot be null'
        almacen.setLatitud(BigDecimal.ZERO);
        almacen.setLongitud(BigDecimal.ZERO);

        almacen = almacenRepository.save(almacen);

        // 3. Guardar Usuario Admin Principal
        Usuario admin = new Usuario();
        admin.setNombre(dto.getNombreAdmin());
        admin.setApat(dto.getApatAdmin());
        admin.setAmat(dto.getAmatAdmin());
        admin.setUsername(dto.getUsername());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setEmail(dto.getCorreo());
        admin.setRol(Rol.ADMIN);
        admin.setBotica(botica);
        admin.setAlmacen(almacen);

        usuarioRepository.save(admin);
    }
}