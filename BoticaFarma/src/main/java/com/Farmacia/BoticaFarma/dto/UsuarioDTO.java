package com.Farmacia.BoticaFarma.dto;

import com.Farmacia.BoticaFarma.model.Rol; // Importa el ENUM
import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer idUsuario;
    private String nombre;
    private String apat;
    private String amat;
    private String username;
    private String password;
    private String rol; // Como String para el frontend
    private String nombreCompleto;
}