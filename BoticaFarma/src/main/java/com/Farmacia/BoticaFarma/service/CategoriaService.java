package com.Farmacia.BoticaFarma.service;

import com.Farmacia.BoticaFarma.dto.CategoriaDTO;
import com.Farmacia.BoticaFarma.model.Categoria;
import com.Farmacia.BoticaFarma.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Listar todas las categorías
    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener categoría por ID
    @Transactional(readOnly = true)
    public CategoriaDTO obtenerCategoriaPorId(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return convertirADTO(categoria);
    }

    // Crear categoría
    @Transactional
    public CategoriaDTO crearCategoria(CategoriaDTO dto) {
        // Validar nombre único
        if (categoriaRepository.existsByNombreCategoria(dto.getNombre_categoria())) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }

        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(dto.getNombre_categoria());

        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return convertirADTO(categoriaGuardada);
    }

    // Actualizar categoría
    @Transactional
    public CategoriaDTO actualizarCategoria(Integer id, CategoriaDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Validar nombre único (excepto la propia)
        if (categoriaRepository.existsByNombreCategoriaAndIdCategoriaNot(dto.getNombre_categoria(), id)) {
            throw new RuntimeException("Ya existe otra categoría con ese nombre");
        }

        categoria.setNombreCategoria(dto.getNombre_categoria());

        Categoria categoriaActualizada = categoriaRepository.save(categoria);
        return convertirADTO(categoriaActualizada);
    }

    // Eliminar categoría
    @Transactional
    public void eliminarCategoria(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoriaRepository.deleteById(id);
    }

    // Convertir entidad a DTO
    private CategoriaDTO convertirADTO(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getIdCategoria(),
                categoria.getNombreCategoria()
        );
    }
}
