package com.dam.eduia.dto;

import lombok.Data;

@Data
public class RespuestaDTO {
    private String respuesta;
    private Long conversacionId;
    private Long mensajeId;

    public RespuestaDTO(String respuesta, Long conversacionId, Long mensajeId) {
        this.respuesta = respuesta;
        this.conversacionId = conversacionId;
        this.mensajeId = mensajeId;
    }
}