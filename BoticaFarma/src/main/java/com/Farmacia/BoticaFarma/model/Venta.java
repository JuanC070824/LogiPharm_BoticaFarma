package com.Farmacia.BoticaFarma.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="Venta")
public class Venta {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idVenta")
    private int idVenta;

    @ManyToOne
    @JoinColumn(name="idUsuario")
    private Usuario usuario;

    @Column(name="fecha")
    private LocalDateTime fecha=LocalDateTime.now();

    @Column(name="total",precision = 10,scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name="metodopago")
    private Metodopago metodopago;

    public Venta() {
        //VACIO
    }

    public Venta(Usuario usuario, LocalDateTime fecha, BigDecimal total, Metodopago metodopago) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.total = total;
        this.metodopago = metodopago;
    }
    //GETTERS Y SETTERS

    public int getIdVenta() {
        return idVenta;
    }
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
}
