package com.Farmacia.BoticaFarma.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name="proveedor")

public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idProveedor")
    private int idProveedor;

    @NotBlank
    @Column(name="nombre_proveedor")
    private String nombreProveedor;

    @NotBlank
    @Column(name="direccion")
    private String direccion;

    @Email
    @NotBlank
    @Column(name="email")
    private String email;

    public Proveedor(){
        //Constructor vacío
    }
    public Proveedor(String nombreProveedor, String direccion, String email) {
        //JPA ya maneja las id
        this.nombreProveedor = nombreProveedor;
        this.direccion = direccion;
        this.email = email;
    }
    //GETTERS Y SETTERS
    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
