package com.Farmacia.BoticaFarma.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistroBoticaDTO {

    // Datos de la Botica
    @NotBlank(message = "El nombre de la botica es obligatorio")
    @Size(max = 100, message = "El nombre de la botica no puede exceder 100 caracteres")
    private String nombreBotica;

    @NotBlank(message = "El RUC es obligatorio")
    @Pattern(regexp = "\\d{11}", message = "El RUC debe tener exactamente 11 dígitos numéricos")
    private String ruc;

    // Datos de la Sucursal Principal inicial
    @NotBlank(message = "El nombre de la sede es obligatorio")
    private String nombreSede; // "Sede Central"

    @NotBlank(message = "La dirección del local es obligatoria")
    private String direccionLocal;

    // Datos del Usuario Administrador (Dueño)
    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo debe contener letras")
    private String nombreAdmin;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido paterno solo debe contener letras")
    private String apatAdmin;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido materno solo debe contener letras")
    private String amatAdmin;

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 4, max = 30, message = "El username debe tener entre 4 y 30 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+$", message = "El username no permite caracteres especiales inapropiados")
    private String username;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    // GETTERS Y SETTERS
    public String getNombreBotica() { return nombreBotica; }
    public void setNombreBotica(String nombreBotica) { this.nombreBotica = nombreBotica; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getNombreSede() { return nombreSede; }
    public void setNombreSede(String nombreSede) { this.nombreSede = nombreSede; }

    public String getDireccionLocal() { return direccionLocal; }
    public void setDireccionLocal(String direccionLocal) { this.direccionLocal = direccionLocal; }

    public String getNombreAdmin() { return nombreAdmin; }
    public void setNombreAdmin(String nombreAdmin) { this.nombreAdmin = nombreAdmin; }

    public String getApatAdmin() { return apatAdmin; }
    public void setApatAdmin(String apatAdmin) { this.apatAdmin = apatAdmin; }

    public String getAmatAdmin() { return amatAdmin; }
    public void setAmatAdmin(String amatAdmin) { this.amatAdmin = amatAdmin; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}