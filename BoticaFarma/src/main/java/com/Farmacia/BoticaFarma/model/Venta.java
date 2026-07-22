package com.Farmacia.BoticaFarma.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="venta")
public class Venta {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idVenta")
    private int idVenta;

    @ManyToOne
    @JoinColumn(name="idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name="idAlmacen", nullable = false)
    private Almacen almacen; // <--- SUCURSAL/LOCAL DONDE SE REALIZÓ LA VENTA

    @Column(name="fecha")
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(name="total", precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name="metodopago")
    private Metodopago metodopago;

    @Column(name = "tipo_venta")
    @Enumerated(EnumType.STRING)
    private TipoVenta tipoVenta;

    public enum TipoVenta {
        MOSTRADOR, DELIVERY
    }

    public Venta() {
        // VACÍO
    }

    public Venta(Usuario usuario, Almacen almacen, LocalDateTime fecha, BigDecimal total, Metodopago metodopago) {
        this.usuario = usuario;
        this.almacen = almacen;
        this.fecha = fecha;
        this.total = total;
        this.metodopago = metodopago;
    }

    // GETTERS Y SETTERS

    public int getIdVenta() {
        return idVenta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Metodopago getMetodopago() {
        return metodopago;
    }

    public void setMetodopago(Metodopago metodopago) {
        this.metodopago = metodopago;
    }

    public TipoVenta getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(TipoVenta tipoVenta) {
        this.tipoVenta = tipoVenta;
    }
}