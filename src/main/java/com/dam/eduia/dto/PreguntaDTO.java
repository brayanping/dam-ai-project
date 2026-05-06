package com.dam.eduia.dto;

public class PreguntaDTO {
    private String pregunta;
    private Long conversacionId;

    public String getPregunta() { return pregunta; }
    public void setPregunta(String pregunta) { this.pregunta = pregunta; }

    public Long getConversacionId() { return conversacionId; }
    public void setConversacionId(Long conversacionId) { this.conversacionId = conversacionId; }
}