package com.Farmacia.BoticaFarma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="Lote")
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idLote")
    private int idLote;

    @ManyToOne
    @JoinColumn(name="idProducto", nullable=false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name="idCompra", nullable=true)
    private Compra compra;

    @NotNull
    @Column(name="codigo_lote", length=50, nullable=false)
    private String codigoLote;

    @Column(name="cantidad_inicial", nullable=false)
    private int cantidadInicial;

    @Column(name="cantidad_actual", nullable=false)
    private int cantidadActual;

    @Column(name="fecha_ingreso", nullable=false)
    private LocalDate fechaIngreso;

    @Column(name="fecha_vencimiento", nullable=false)
    private LocalDate fechaVencimiento;

    @Column(name="precio_compra", precision=10, scale=2, nullable=false)
    private BigDecimal precioCompra;

    @Enumerated(EnumType.STRING)
    @Column(name="estado_lote", nullable=false)
    private EstadoLote estadoLote;


    public Lote(){
        //vacio
    }
    public Lote(int idLote, Producto producto, Compra compra, String codigoLote, int cantidadInicial, int cantidadActual, LocalDate fechaIngreso, LocalDate fechaVencimiento, BigDecimal precioCompra, EstadoLote estadoLote) {
        this.idLote = idLote;
        this.producto = producto;
        this.compra = compra;
        this.codigoLote = codigoLote;
        this.cantidadInicial = cantidadInicial;
        this.cantidadActual = cantidadActual;
        this.fechaIngreso = fechaIngreso;
        this.fechaVencimiento = fechaVencimiento;
        this.precioCompra = precioCompra;
        this.estadoLote = estadoLote;
    }

    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public String getCodigoLote() {
        return codigoLote;
    }

    public void setCodigoLote(String codigoLote) {
        this.codigoLote = codigoLote;
    }

    public int getCantidadInicial() {
        return cantidadInicial;
    }

    public void setCantidadInicial(int cantidadInicial) {
        this.cantidadInicial = cantidadInicial;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public EstadoLote getEstadoLote() {
        return estadoLote;
    }

    public void setEstadoLote(EstadoLote estadoLote) {
        this.estadoLote = estadoLote;
    }
}
