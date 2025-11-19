package com.Farmacia.BoticaFarma.dto;

import com.Farmacia.BoticaFarma.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private int idUsuario;
    private String nombre;
    private String apat;
    private String amat;
    private String username;
    private Rol rol;
}
