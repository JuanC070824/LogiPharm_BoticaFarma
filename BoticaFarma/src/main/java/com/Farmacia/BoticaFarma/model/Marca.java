package com.Farmacia.BoticaFarma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="Marca")
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idMarca")
    private int idMarca;

    @NotBlank(message = "El nombre de la marca no puede estar vacío")
    @Column(name="nombre_marca", length=60, nullable=false)
    private String nombreMarca;

    @ManyToOne
    @JoinColumn(name="idProveedor", nullable=false)
    private Proveedor proveedor;

    // Constructor vacío
    public Marca() {
    }

    // Constructor con parámetros
    public Marca(String nombreMarca, Proveedor proveedor) {
        this.nombreMarca = nombreMarca;
        this.proveedor = proveedor;
    }

    // Getters y Setters
    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}