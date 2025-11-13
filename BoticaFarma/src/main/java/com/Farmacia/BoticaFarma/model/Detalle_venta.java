package com.Farmacia.BoticaFarma.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Detalle_venta")
public class Detalle_venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idDetalle_venta")
    private int idDetalle_venta;

    @ManyToOne
    @JoinColumn(name="idVenta")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name="idProducto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name="idCliente")
    private Cliente cliente;

    @Column(name="cantidad",nullable = false)
    private int cantidad;

    @Column(name="precio_unitario",precision = 10,scale = 2,nullable = false)
    private BigDecimal precio_unitario;

    @Column(name="subtotal",precision = 10,scale=2,nullable = false)
    private BigDecimal subtotal;

    @OneToMany(mappedBy="detalleVenta", cascade = CascadeType.ALL)
    private Set<DetalleVentaLote> lotesAfectados = new HashSet<>();


    public Detalle_venta() {
        //VACIO
    }

    public Detalle_venta(Venta venta, Producto producto, Cliente cliente, int cantidad, BigDecimal precio_unitario, BigDecimal subtotal) {
        this.venta = venta;
        this.producto = producto;
        this.cliente = cliente;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.subtotal = subtotal;
    }


    //GETTERS Y SETTERS


    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
