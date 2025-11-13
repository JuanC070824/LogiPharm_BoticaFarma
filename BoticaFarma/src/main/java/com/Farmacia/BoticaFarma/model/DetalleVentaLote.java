package com.Farmacia.BoticaFarma.model;

import jakarta.persistence.*;

@Entity
@Table(name="Detalle_venta_lote")
public class DetalleVentaLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idDetalle_venta_lote")
    private int idDetalleVentaLote;

    @ManyToOne
    @JoinColumn(name="idDetalle_venta", nullable=false)
    private Detalle_venta detalleVenta;

    @ManyToOne
    @JoinColumn(name="idLote", nullable=false)
    private Lote lote;

    @Column(name="cantidad_descontada", nullable=false)
    private int cantidadDescontada;

    // Constructor vacío
    public DetalleVentaLote() {
    }

    // Constructor con parámetros
    public DetalleVentaLote(Detalle_venta detalleVenta, Lote lote, int cantidadDescontada) {
        this.detalleVenta = detalleVenta;
        this.lote = lote;
        this.cantidadDescontada = cantidadDescontada;
    }

    // Getters y Setters
    public int getIdDetalleVentaLote() {
        return idDetalleVentaLote;
    }

    public void setIdDetalleVentaLote(int idDetalleVentaLote) {
        this.idDetalleVentaLote = idDetalleVentaLote;
    }

    public Detalle_venta getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(Detalle_venta detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public int getCantidadDescontada() {
        return cantidadDescontada;
    }

    public void setCantidadDescontada(int cantidadDescontada) {
        this.cantidadDescontada = cantidadDescontada;
    }
}