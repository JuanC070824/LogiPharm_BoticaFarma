package com.Farmacia.BoticaFarma.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "botica")
public class Botica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idBotica")
    private int idBotica;

    @NotBlank(message = "El nombre de la botica es obligatorio")
    @Column(name = "nombre_botica", nullable = false)
    private String nombreBotica;

    @Column(name = "ruc")
    private String ruc;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    public Botica() {
    }

    public Botica(String nombreBotica, String ruc) {
        this.nombreBotica = nombreBotica;
        this.ruc = ruc;
    }

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    // GETTERS Y SETTERS
    public int getIdBotica() {
        return idBotica;
    }

    public void setIdBotica(int idBotica) {
        this.idBotica = idBotica;
    }

    public String getNombreBotica() {
        return nombreBotica;
    }

    public void setNombreBotica(String nombreBotica) {
        this.nombreBotica = nombreBotica;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}