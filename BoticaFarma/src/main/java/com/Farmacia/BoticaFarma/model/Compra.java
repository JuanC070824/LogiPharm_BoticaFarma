package com.Farmacia.BoticaFarma.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="Compra")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idCompra")
    private int idCompra;

    @ManyToOne
    @JoinColumn(name="iDUsuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name="idAlmacen", nullable=true)
    private Almacen almacen;


    @Column(name="fecha")
    private LocalDateTime fecha=LocalDateTime.now();


    @Column(name="total",precision = 10,scale=2)
    private BigDecimal valor;

    public Compra() {
        //VACIO
    }

    public Compra(Usuario usuario, Almacen almacen, LocalDateTime fecha, BigDecimal valor) {
        this.usuario = usuario;
        this.almacen = almacen;
        this.fecha = fecha;
        this.valor = valor;
    }
    //GETTERS Y SETTERS

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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
