package com.dam.eduia.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "documentos")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String tipo; // PDF, TXT, MD...

    @Column(nullable = false)
    private String rutaArchivo;

    @Column
    private boolean procesado = false;

    @Column
    private LocalDateTime creadoEn = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User usuario;
}