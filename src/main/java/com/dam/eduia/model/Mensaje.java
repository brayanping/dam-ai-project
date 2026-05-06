package com.dam.eduia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(nullable = false)
    private String rol;

    @Column
    private LocalDateTime creadoEn = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "conversacion_id", nullable = false)
    private Conversacion conversacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public Conversacion getConversacion() { return conversacion; }
    public void setConversacion(Conversacion conversacion) { this.conversacion = conversacion; }
}