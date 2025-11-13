package com.Farmacia.BoticaFarma.service;

import com.Farmacia.BoticaFarma.dto.MarcaDTO;
import com.Farmacia.BoticaFarma.model.Marca;
import com.Farmacia.BoticaFarma.model.Proveedor;
import com.Farmacia.BoticaFarma.repository.MarcaRepository;
import com.Farmacia.BoticaFarma.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    // Listar todas las marcas
    @Transactional(readOnly = true)
    public List<MarcaDTO> listarMarcas() {
        return marcaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener marca por ID
    @Transactional(readOnly = true)
    public MarcaDTO obtenerMarcaPorId(Integer id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        return convertirADTO(marca);
    }

    // Listar marcas por proveedor
    @Transactional(readOnly = true)
    public List<MarcaDTO> listarMarcasPorProveedor(Integer idProveedor) {
        return marcaRepository.findByProveedorId(idProveedor).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Crear marca
    @Transactional
    public MarcaDTO crearMarca(MarcaDTO dto) {
        // Validar nombre único
        if (marcaRepository.existsByNombreMarca(dto.getNombreMarca())) {
            throw new RuntimeException("Ya existe una marca con ese nombre");
        }

        // Validar que existe el proveedor
        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Marca marca = new Marca();
        marca.setNombreMarca(dto.getNombreMarca());
        marca.setProveedor(proveedor);

        Marca marcaGuardada = marcaRepository.save(marca);
        return convertirADTO(marcaGuardada);
    }

    // Actualizar marca
    @Transactional
    public MarcaDTO actualizarMarca(Integer id, MarcaDTO dto) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        // Validar nombre único (excepto la propia)
        if (marcaRepository.existsByNombreMarcaAndIdMarcaNot(dto.getNombreMarca(), id)) {
            throw new RuntimeException("Ya existe otra marca con ese nombre");
        }

        // Validar proveedor
        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        marca.setNombreMarca(dto.getNombreMarca());
        marca.setProveedor(proveedor);

        Marca marcaActualizada = marcaRepository.save(marca);
        return convertirADTO(marcaActualizada);
    }

    // Eliminar marca
    @Transactional
    public void eliminarMarca(Integer id) {
        if (!marcaRepository.existsById(id)) {
            throw new RuntimeException("Marca no encontrada");
        }
        marcaRepository.deleteById(id);
    }

    // Convertir entidad a DTO
    private MarcaDTO convertirADTO(Marca marca) {
        return new MarcaDTO(
                marca.getIdMarca(),
                marca.getNombreMarca(),
                marca.getProveedor().getIdProveedor(),
                marca.getProveedor().getNombreProveedor()
        );
    }
}