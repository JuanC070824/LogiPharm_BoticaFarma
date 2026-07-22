package com.Farmacia.BoticaFarma.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCategoria")
    private int idCategoria;

    @NotBlank
    @Column(name = "nombre_categoria")
    private String nombreCategoria; //  camelCase para JPA

    public Categoria() {
        // Constructor vacío
    }

    public Categoria(String nombreCategoria) {
        // JPA gestiona el ID
        this.nombreCategoria = nombreCategoria;
    }

    // GETTERS Y SETTERS
    public int getIdCategoria() {
        return this.idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return this.nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}
