package com.Farmacia.BoticaFarma.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MarcaDTO {
    private Integer idMarca;

    @NotBlank(message = "El nombre de la marca es obligatorio")
    private String nombreMarca;

    @NotNull(message = "El proveedor es obligatorio")
    private Integer idProveedor;

    private String nombreProveedor;

    // Constructor vacío
    public MarcaDTO() {}

    // Constructor completo
    public MarcaDTO(Integer idMarca, String nombreMarca, Integer idProveedor, String nombreProveedor) {
        this.idMarca = idMarca;
        this.nombreMarca = nombreMarca;
        this.idProveedor = idProveedor;
        this.nombreProveedor = nombreProveedor;
    }

    // Getters y Setters
    public Integer getIdMarca() { return idMarca; }
    public void setIdMarca(Integer idMarca) { this.idMarca = idMarca; }

    public String getNombreMarca() { return nombreMarca; }
    public void setNombreMarca(String nombreMarca) { this.nombreMarca = nombreMarca; }

    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }

    public String getNombreProveedor() { return nombreProveedor; }
    public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }
}