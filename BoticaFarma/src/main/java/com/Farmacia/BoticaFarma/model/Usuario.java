package com.Farmacia.BoticaFarma.model;
import com.Farmacia.BoticaFarma.model.Rol;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name="Usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idUsuario")
    private int idUsuario;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name="nombre")
    private String nombre;
    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    @Column(name="apat")
    private String apat;
    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    @Column(name="amat")
    private String amat;

    @NotBlank(message = "El username no puede estar vacío")
    @Column(name="username")
    private String username;
    @NotBlank(message = "El password no puede estar vacío")
    @Column(name="password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name="rol")
    private Rol rol;

    public Usuario() {
        //Constructor vacío
    }
    public Usuario(String nombre, String apat, String amat, String username, String password, Rol rol) {
        //JPA ya maneja las id=
        this.nombre = nombre;
        this.apat = apat;
        this.amat = amat;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }
    //GETTERS Y SETTERS
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApat() {
        return apat;
    }

    public void setApat(String apat) {
        this.apat = apat;
    }

    public String getAmat() {
        return amat;
    }

    public void setAmat(String amat) {
        this.amat = amat;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
