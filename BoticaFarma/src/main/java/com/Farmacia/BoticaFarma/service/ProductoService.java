package com.Farmacia.BoticaFarma.service;

import com.Farmacia.BoticaFarma.dto.CreateProductoDTO;
import com.Farmacia.BoticaFarma.dto.ProductoDTO;
import com.Farmacia.BoticaFarma.model.Botica;
import com.Farmacia.BoticaFarma.model.Categoria;
import com.Farmacia.BoticaFarma.model.Marca;
import com.Farmacia.BoticaFarma.model.Producto;
import com.Farmacia.BoticaFarma.repository.BoticaRepository;
import com.Farmacia.BoticaFarma.repository.CategoriaRepository;
import com.Farmacia.BoticaFarma.repository.LoteRepository;
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

    @Autowired
    private LoteRepository loteRepository;

    // Listar productos filtrados por Botica y con stock de la sucursal actual
    @Transactional(readOnly = true)
    public Page<ProductoDTO> listarProductos(Integer idBotica, Integer idAlmacen, Pageable pageable) {
        return productoRepository.findByBotica_IdBoticaAndNombreProductoContainingIgnoreCase(idBotica, "", pageable)
                .map(p -> convertirADTO(p, idAlmacen));
    }

    // Buscar productos con filtros y Botica
    @Transactional(readOnly = true)
    public Page<ProductoDTO> buscarProductos(Integer idBotica, Integer idAlmacen, String nombre, Integer idCategoria,
                                             Integer idMarca, Pageable pageable) {
        return productoRepository.buscarConFiltros(idBotica, nombre, idCategoria, idMarca, pageable)
                .map(p -> convertirADTO(p, idAlmacen));
    }

    // Obtener producto por ID (con stock de la sucursal actual)
    @Transactional(readOnly = true)
    public ProductoDTO obtenerProductoPorId(Integer id, Integer idAlmacen) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        return convertirADTO(producto, idAlmacen);
    }

    // Crear producto asignándolo a la Botica logueada
    @Transactional
    public ProductoDTO crearProducto(Integer idBotica, Integer idAlmacen, CreateProductoDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Marca marca = marcaRepository.findById(dto.getIdMarca())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        Botica botica = boticaRepository.findById(idBotica)
                .orElseThrow(() -> new RuntimeException("Botica no encontrada"));

        Producto producto = new Producto();
        producto.setNombreProducto(dto.getNombre_producto());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(0);
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setBotica(botica);

        Producto productoGuardado = productoRepository.save(producto);
        return convertirADTO(productoGuardado, idAlmacen);
    }

    // Actualizar producto
    @Transactional
    public ProductoDTO actualizarProducto(Integer id, Integer idAlmacen, CreateProductoDTO dto) {
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
        return convertirADTO(productoActualizado, idAlmacen);
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
    public Page<ProductoDTO> productosStockBajo(Integer idBotica, Integer idAlmacen, Integer minStock, Pageable pageable) {
        return productoRepository.findProductosStockBajo(idBotica, minStock, pageable)
                .map(p -> convertirADTO(p, idAlmacen));
    }

    // Convertir Entidad a DTO (siempre recibe idAlmacen para calcular el stock de esa sucursal)
    private ProductoDTO convertirADTO(Producto producto, Integer idAlmacen) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setNombre_producto(producto.getNombreProducto());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(loteRepository.calcularStockPorAlmacen(producto.getIdProducto(), idAlmacen));

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