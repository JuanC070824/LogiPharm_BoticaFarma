package com.Farmacia.BoticaFarma.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;



@Entity
@Table(name="Cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idCliente")
    private int idCliente;

    @NotBlank
    @Column(name="nombre")
    private String nombre;

    @NotBlank
    @Column(name="apellido_pat")
    private String apellido_pat;

    @NotBlank
    @Column(name="apellido_mat")
    private String apellido_mat;

    @NotBlank
    @Column(name="DNI",length=8,nullable = false)
    private int DNI;

    @NotBlank
    @Column(name="RUC",length=11)
    private Integer RUC;


    public Cliente(){
        //VACIOOOOOOO
    }
    //GETTERS Y SETTERS
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

    public String getApellido_pat() {
        return apellido_pat;
    }

    public void setApellido_pat(String apellido_pat) {
        this.apellido_pat = apellido_pat;
    }

    public String getApellido_mat() {
        return apellido_mat;
    }

    public void setApellido_mat(String apellido_mat) {
        this.apellido_mat = apellido_mat;
    }

    public int getDNI() {
        return DNI;
    }

    public void setDNI(int DNI) {
        this.DNI = DNI;
    }

    public Integer getRUC() { return RUC; }
    public void setRUC(Integer RUC) { this.RUC = RUC; }
}
