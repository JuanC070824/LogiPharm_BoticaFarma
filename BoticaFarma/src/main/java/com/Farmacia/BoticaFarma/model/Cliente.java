package com.Farmacia.BoticaFarma.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Digits;

@Entity
@Table(name="cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idCliente")
    private int idCliente;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name="nombre")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Column(name="apellido_pat")
    private String apellidoPat;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Column(name="apellido_mat")
    private String apellidoMat;

    @NotNull(message = "El DNI es obligatorio")
    @Min(value = 10000000, message = "El DNI debe tener 8 dígitos")
    @Max(value = 99999999, message = "El DNI debe tener 8 dígitos")
    @Column(name="DNI")
    private int DNI;

    // RUC es opcional pero si se ingresa debe tener 11 dígitos
    @Min(value = 10000000000L, message = "El RUC debe tener 11 dígitos")
    @Max(value = 99999999999L, message = "El RUC debe tener 11 dígitos")
    @Column(name="RUC")
    private Long RUC;  // ← CAMBIO: Long en lugar de Integer

    public Cliente(){
        // Constructor vacío
    }

    // GETTERS Y SETTERS
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPat() {
        return apellidoPat;
    }

    public void setApellidoPat(String apellidoPat) {
        this.apellidoPat = apellidoPat;
    }

    public String getApellidoMat() {
        return apellidoMat;
    }

    public void setApellidoMat(String apellidoMat) {
        this.apellidoMat = apellidoMat;
    }

    public int getDNI() {
        return DNI;
    }

    public void setDNI(int DNI) {
        this.DNI = DNI;
    }

    public Long getRUC() {
        return RUC;
    }

    public void setRUC(Long RUC) {
        this.RUC = RUC;
    }
}