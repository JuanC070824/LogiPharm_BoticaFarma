package com.Farmacia.BoticaFarma.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name="Producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idProducto")
    private int idProducto;

    @ManyToOne
    @JoinColumn(name="idCategoria",nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name="idMarca", nullable=false)
    private Marca marca;

    @NotBlank
    @Column(name="nombre_producto")
    private String nombreProducto;

    @NotNull(message = "El precio no puede ser nulo")  // BigDecimal
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Column(name="precio", precision=10, scale=2, nullable=false)
    private BigDecimal Precio;

    @Column(name="Stock",nullable = false)
    private int Stock;

    @ManyToMany
    @JoinTable(
            name="Producto_Almacen",
            joinColumns = @JoinColumn(name="idProducto"),
            inverseJoinColumns= @JoinColumn(name="idAlmacen")
    )
    private Set<Almacen> almacenes;

    public Producto() {
        //VACIO
    }
    public Producto(Categoria categoria, Marca marca, String nombreProducto,
                    BigDecimal precio, int stock,
                    Set<Almacen> almacenes) {
        this.categoria = categoria;
        this.marca = marca;
        this.nombreProducto = nombreProducto;
        this.Precio = precio;
        this.Stock = stock;
        this.almacenes = almacenes;
    }

    //SETTERS Y GETTERS

    public int getIdProducto() {
        return idProducto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public BigDecimal getPrecio() {
        return Precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.Precio = precio;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        this.Stock = stock;
    }

    public Set<Almacen> getAlmacenes() {
        return almacenes;
    }

    public void setAlmacenes(Set<Almacen> almacenes) {
        this.almacenes = almacenes;
    }
}
