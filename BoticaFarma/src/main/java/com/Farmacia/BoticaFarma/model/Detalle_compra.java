package com.Farmacia.BoticaFarma.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Entity
@Table(name="Detalle_compra")
public class Detalle_compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idDetalle_compra")
    private int idDetalle_compra;

    @ManyToOne
    @JoinColumn(name="idCompra")
    private Compra compra;

    @ManyToOne
    @JoinColumn(name="idProducto")
    private Producto producto;

    @Column(name="cantidad")
    private int cantidad;

    @Column(name="precio_unitario",precision = 10,scale = 2)
    private BigDecimal precio_unitario;

    @Column(name="subtotal",precision = 10,scale = 2)
    private BigDecimal subtotal;

    public Detalle_compra() {
        //VACIO
    }

    public Detalle_compra(Compra compra, Producto producto, int cantidad, BigDecimal precio_unitario, BigDecimal subtotal) {
        this.compra = compra;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.subtotal = subtotal;
    }

    //SETTERS Y GETTERS


    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(BigDecimal precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
