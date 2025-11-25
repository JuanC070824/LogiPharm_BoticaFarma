package com.Farmacia.BoticaFarma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor // Para poder hacer 'new ReporteVentaDTO(...)' en el servicio
public class ReporteVentaDTO {
    private String etiqueta;
    private Double total;
}