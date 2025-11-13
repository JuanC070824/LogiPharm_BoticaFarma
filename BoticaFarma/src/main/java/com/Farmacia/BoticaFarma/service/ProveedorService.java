package com.Farmacia.BoticaFarma.service;

import com.Farmacia.BoticaFarma.dto.ProveedorDTO;
import com.Farmacia.BoticaFarma.model.Proveedor;
import com.Farmacia.BoticaFarma.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    // Listar todos los proveedores
    @Transactional(readOnly = true)
    public List<ProveedorDTO> listarProveedores() {
        return proveedorRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener proveedor por ID
    @Transactional(readOnly = true)
    public ProveedorDTO obtenerProveedorPorId(Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        return convertirADTO(proveedor);
    }

    // Crear proveedor
    @Transactional
    public ProveedorDTO crearProveedor(ProveedorDTO dto) {
        // Validar email único
        if (proveedorRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un proveedor con ese email");
        }

        Proveedor proveedor = new Proveedor();
        proveedor.setNombreProveedor(dto.getNombreProveedor());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setEmail(dto.getEmail());

        Proveedor proveedorGuardado = proveedorRepository.save(proveedor);
        return convertirADTO(proveedorGuardado);
    }

    // Actualizar proveedor
    @Transactional
    public ProveedorDTO actualizarProveedor(Integer id, ProveedorDTO dto) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        // Validar email único (excepto el propio)
        if (proveedorRepository.existsByEmailAndIdProveedorNot(dto.getEmail(), id)) {
            throw new RuntimeException("Ya existe otro proveedor con ese email");
        }

        proveedor.setNombreProveedor(dto.getNombreProveedor());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setEmail(dto.getEmail());

        Proveedor proveedorActualizado = proveedorRepository.save(proveedor);
        return convertirADTO(proveedorActualizado);
    }

    // Eliminar proveedor
    @Transactional
    public void eliminarProveedor(Integer id) {
        if (!proveedorRepository.existsById(id)) {
            throw new RuntimeException("Proveedor no encontrado");
        }
        proveedorRepository.deleteById(id);
    }

    // Buscar proveedores por nombre
    @Transactional(readOnly = true)
    public List<ProveedorDTO> buscarPorNombre(String nombre) {
        return proveedorRepository.findByNombreProveedorContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Convertir entidad a DTO
    private ProveedorDTO convertirADTO(Proveedor proveedor) {
        return new ProveedorDTO(
                proveedor.getIdProveedor(),
                proveedor.getNombreProveedor(),
                proveedor.getDireccion(),
                proveedor.getEmail()
        );
    }
}