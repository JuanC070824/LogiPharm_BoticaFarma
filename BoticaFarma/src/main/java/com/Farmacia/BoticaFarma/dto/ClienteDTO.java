package com.Farmacia.BoticaFarma.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private int idCliente;
    private String nombre;
    private String apellido_pat;
    private String apellido_mat;
    private int DNI;
    private Integer RUC;
}