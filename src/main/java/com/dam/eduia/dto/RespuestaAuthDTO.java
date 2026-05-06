package com.dam.eduia.dto;

import lombok.Data;

@Data
public class RespuestaAuthDTO {
    private String token;
    private String username;
    private String email;

    public RespuestaAuthDTO(String token, String username, String email) {
        this.token = token;
        this.username = username;
        this.email = email;
    }
}