package com.Farmacia.BoticaFarma.dto;

import java.math.BigDecimal;

public class ProductoDTO {
    private Integer idProducto;
    private String nombre_producto;
    private BigDecimal precio;
    private Integer stock;
    private Integer idCategoria;
    private String nombreCategoria;
    private Integer idMarca;
    private String nombreMarca;
    private Integer idProveedor;
    private String nombreProveedor;

    // Constructor vacío
    public ProductoDTO() {}

    // Constructor completo
    public ProductoDTO(Integer idProducto, String nombre_producto, BigDecimal precio,
                       Integer stock, Integer idCategoria, String nombreCategoria,
                       Integer idMarca, String nombreMarca, Integer idProveedor,
                       String nombreProveedor) {
        this.idProducto = idProducto;
        this.nombre_producto = nombre_producto;
        this.precio = precio;
        this.stock = stock;
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.idMarca = idMarca;
        this.nombreMarca = nombreMarca;
        this.idProveedor = idProveedor;
        this.nombreProveedor = nombreProveedor;
    }

    // Getters y Setters
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

    public String getNombre_producto() { return nombre_producto; }
    public void setNombre_producto(String nombre_producto) { this.nombre_producto = nombre_producto; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }

    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    public Integer getIdMarca() { return idMarca; }
    public void setIdMarca(Integer idMarca) { this.idMarca = idMarca; }

    public String getNombreMarca() { return nombreMarca; }
    public void setNombreMarca(String nombreMarca) { this.nombreMarca = nombreMarca; }

    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }

    public String getNombreProveedor() { return nombreProveedor; }
    public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }
}