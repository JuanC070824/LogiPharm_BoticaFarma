package com.Farmacia.BoticaFarma.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Almacen")
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idAlmacen")
    private int idAlmacen;

    /*Osea, un almacen esta asociado a un solo proveedor,
    pero un proveedor tiene varios almacenes*/
    @ManyToOne
    @JoinColumn(name="idProveedor",nullable = false)
    private Proveedor proveedor;

    @NotNull
    @Column(name="latitud",precision = 10,scale = 7)
    private BigDecimal latitud;

    @NotNull
    @Column(name="longitud",precision = 10,scale = 7)
    private BigDecimal longitud;

    public Almacen(){
        //VACIOOOOOOOOOOO
    }

    public Almacen(Proveedor proveedor, BigDecimal latitud, BigDecimal longitud) {
        //No es necesario id en el constructor
        this.proveedor = proveedor;
        this.latitud = latitud;
        this.longitud = longitud;
    }
    //GETTERS Y SETTERS
    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }
}
