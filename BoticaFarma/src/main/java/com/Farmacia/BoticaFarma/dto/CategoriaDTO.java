package com.Farmacia.BoticaFarma.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoriaDTO {
    private Integer idCategoria;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombre_categoria;

    // Constructor vacío
    public CategoriaDTO() {}

    // Constructor completo
    public CategoriaDTO(Integer idCategoria, String nombre_categoria) {
        this.idCategoria = idCategoria;
        this.nombre_categoria = nombre_categoria;
    }

    // Getters y Setters
    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre_categoria() { return nombre_categoria; }
    public void setNombre_categoria(String nombre_categoria) { this.nombre_categoria = nombre_categoria; }
}