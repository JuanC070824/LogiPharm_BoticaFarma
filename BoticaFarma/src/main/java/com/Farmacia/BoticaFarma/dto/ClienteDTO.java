package com.Farmacia.BoticaFarma.dto;

import lombok.Data;

@Data
public class ClienteDTO {
    private Integer idCliente;
    private String nombre;
    private String apellidoPat;
    private String apellidoMat;
    private Integer dni;
    private Long ruc;  // ← CAMBIO: Long en lugar de Integer
    private String nombreCompleto;
}
