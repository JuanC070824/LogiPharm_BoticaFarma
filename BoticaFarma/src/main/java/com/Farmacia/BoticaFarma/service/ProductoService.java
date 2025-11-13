package com.Farmacia.BoticaFarma.service;

import com.Farmacia.BoticaFarma.dto.CreateProductoDTO;
import com.Farmacia.BoticaFarma.dto.ProductoDTO;
import com.Farmacia.BoticaFarma.model.Categoria;
import com.Farmacia.BoticaFarma.model.Marca;
import com.Farmacia.BoticaFarma.model.Producto;
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

    // Listar todos los productos con paginación
    @Transactional(readOnly = true)
    public Page<ProductoDTO> listarProductos(Pageable pageable) {
        return productoRepository.findAll(pageable)
                .map(this::convertirADTO);
    }

    // Buscar productos con filtros
    @Transactional(readOnly = true)
    public Page<ProductoDTO> buscarProductos(String nombre, Integer idCategoria,
                                             Integer idMarca, Pageable pageable) {
        return productoRepository.buscarConFiltros(nombre, idCategoria, idMarca, pageable)
                .map(this::convertirADTO);
    }

    // Obtener producto por ID
    @Transactional(readOnly = true)
    public ProductoDTO obtenerProductoPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        return convertirADTO(producto);
    }

    // Crear producto (CORREGIDO - solo una vez)
    @Transactional
    public ProductoDTO crearProducto(CreateProductoDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Marca marca = marcaRepository.findById(dto.getIdMarca())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        Producto producto = new Producto();
        producto.setNombreProducto(dto.getNombre_producto());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(0);  // Stock inicial en 0
        producto.setCategoria(categoria);
        producto.setMarca(marca);

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
        // NO actualizamos stock aquí, se maneja por lotes
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

    // Productos con stock bajo
    @Transactional(readOnly = true)
    public Page<ProductoDTO> productosStockBajo(Integer minStock, Pageable pageable) {
        return productoRepository.findProductosStockBajo(minStock, pageable)
                .map(this::convertirADTO);
    }

    // Convertir Entidad a DTO (AGREGADO)
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