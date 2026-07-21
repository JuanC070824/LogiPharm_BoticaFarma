package com.Farmacia.BoticaFarma.service;

import com.Farmacia.BoticaFarma.dto.CreateProductoDTO;
import com.Farmacia.BoticaFarma.dto.ProductoDTO;
import com.Farmacia.BoticaFarma.model.Botica;
import com.Farmacia.BoticaFarma.model.Categoria;
import com.Farmacia.BoticaFarma.model.Marca;
import com.Farmacia.BoticaFarma.model.Producto;
import com.Farmacia.BoticaFarma.repository.BoticaRepository;
import com.Farmacia.BoticaFarma.repository.CategoriaRepository;
import com.Farmacia.BoticaFarma.repository.MarcaRepository;
import com.Farmacia.BoticaFarma.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private BoticaRepository boticaRepository;

    // Listar productos filtrados por Botica
    @Transactional(readOnly = true)
    public Page<ProductoDTO> listarProductos(Integer idBotica, Pageable pageable) {
        return productoRepository.findByBotica_IdBoticaAndNombreProductoContainingIgnoreCase(idBotica, "", pageable)
                .map(this::convertirADTO);
    }

    // Buscar productos con filtros y Botica
    @Transactional(readOnly = true)
    public Page<ProductoDTO> buscarProductos(Integer idBotica, String nombre, Integer idCategoria,
                                             Integer idMarca, Pageable pageable) {
        return productoRepository.buscarConFiltros(idBotica, nombre, idCategoria, idMarca, pageable)
                .map(this::convertirADTO);
    }

    // Obtener producto por ID (verificando que pertenezca a la botica)
    @Transactional(readOnly = true)
    public ProductoDTO obtenerProductoPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        return convertirADTO(producto);
    }

    // Crear producto asignándolo a la Botica logueada
    @Transactional
    public ProductoDTO crearProducto(Integer idBotica, CreateProductoDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Marca marca = marcaRepository.findById(dto.getIdMarca())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        Botica botica = boticaRepository.findById(idBotica)
                .orElseThrow(() -> new RuntimeException("Botica no encontrada"));

        Producto producto = new Producto();
        producto.setNombreProducto(dto.getNombre_producto());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(0);  // Stock inicial en 0
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setBotica(botica); // <--- VINCULACIÓN MULTI-TENANT

        Producto productoGuardado = productoRepository.save(producto);
        return convertirADTO(productoGuardado);
    }

    // Actualizar producto
    @Transactional
    public ProductoDTO actualizarProducto(Integer id, CreateProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Marca marca = marcaRepository.findById(dto.getIdMarca())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        producto.setNombreProducto(dto.getNombre_producto());
        producto.setPrecio(dto.getPrecio());
        producto.setCategoria(categoria);
        producto.setMarca(marca);

        Producto productoActualizado = productoRepository.save(producto);
        return convertirADTO(productoActualizado);
    }

    // Eliminar producto
    @Transactional
    public void eliminarProducto(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    // Productos con stock bajo por Botica
    @Transactional(readOnly = true)
    public Page<ProductoDTO> productosStockBajo(Integer idBotica, Integer minStock, Pageable pageable) {
        return productoRepository.findProductosStockBajo(idBotica, minStock, pageable)
                .map(this::convertirADTO);
    }

    // Convertir Entidad a DTO
    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setNombre_producto(producto.getNombreProducto());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());

        if (producto.getCategoria() != null) {
            dto.setIdCategoria(producto.getCategoria().getIdCategoria());
            dto.setNombreCategoria(producto.getCategoria().getNombreCategoria());
        }

        if (producto.getMarca() != null) {
            dto.setIdMarca(producto.getMarca().getIdMarca());
            dto.setNombreMarca(producto.getMarca().getNombreMarca());

            if (producto.getMarca().getProveedor() != null) {
                dto.setIdProveedor(producto.getMarca().getProveedor().getIdProveedor());
                dto.setNombreProveedor(producto.getMarca().getProveedor().getNombreProveedor());
            }
        }

        return dto;
    }
}