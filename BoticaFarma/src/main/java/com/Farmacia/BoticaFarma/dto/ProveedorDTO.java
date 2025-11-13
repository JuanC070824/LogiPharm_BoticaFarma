package com.Farmacia.BoticaFarma.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ProveedorDTO {
    private Integer idProveedor;

    @NotBlank(message = "El nombre del proveedor es obligatorio")
    private String nombreProveedor;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @Email(message = "Email inválido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    // Constructor vacío
    public ProveedorDTO() {}

    // Constructor completo
    public ProveedorDTO(Integer idProveedor, String nombreProveedor, String direccion, String email) {
        this.idProveedor = idProveedor;
        this.nombreProveedor = nombreProveedor;
        this.direccion = direccion;
        this.email = email;
    }

    // Getters y Setters
    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }

    public String getNombreProveedor() { return nombreProveedor; }
    public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}