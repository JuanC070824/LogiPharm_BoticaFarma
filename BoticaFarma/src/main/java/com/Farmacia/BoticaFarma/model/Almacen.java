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
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "almacen")
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAlmacen")
    private int idAlmacen;

    @ManyToOne
    @JoinColumn(name = "idBotica", nullable = false)
    private Botica botica;

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Column(name = "nombre_sucursal", nullable = false)
    private String nombreSucursal;

    @Column(name = "direccion")
    private String direccion;

    // Cambiado a nullable = true
    @ManyToOne
    @JoinColumn(name = "idProveedor", nullable = true)
    private Proveedor proveedor;

    @Column(name = "latitud", precision = 10, scale = 7, nullable = true)
    private BigDecimal latitud;

    @Column(name = "longitud", precision = 10, scale = 7, nullable = true)
    private BigDecimal longitud;

    public Almacen() {
    }

    public Almacen(Botica botica, String nombreSucursal, String direccion) {
        this.botica = botica;
        this.nombreSucursal = nombreSucursal;
        this.direccion = direccion;
    }

    // GETTERS Y SETTERS
    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public Botica getBotica() {
        return botica;
    }

    public void setBotica(Botica botica) {
        this.botica = botica;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

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