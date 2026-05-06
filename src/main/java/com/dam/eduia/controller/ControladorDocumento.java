package com.dam.eduia.controller;

import com.dam.eduia.model.Documento;
import com.dam.eduia.model.User;
import com.dam.eduia.repository.DocumentoRepositorio;
import com.dam.eduia.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/documentos")
public class ControladorDocumento {

    @Autowired
    private DocumentoRepositorio documentoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    // Sube un documento al sistema
    @PostMapping("/subir")
    public ResponseEntity<Documento> subirDocumento(
            @RequestParam("archivo") MultipartFile archivo,
            Authentication auth) throws IOException {

        User usuario = usuarioRepositorio.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Guarda el archivo en disco
        String carpeta = "documentos/";
        Files.createDirectories(Paths.get(carpeta));
        Path ruta = Paths.get(carpeta + archivo.getOriginalFilename());
        Files.write(ruta, archivo.getBytes());

        // Registra el documento en la base de datos
        Documento documento = new Documento();
        documento.setNombre(archivo.getOriginalFilename());
        documento.setTipo(archivo.getContentType());
        documento.setRutaArchivo(ruta.toString());
        documento.setUsuario(usuario);

        documentoRepositorio.save(documento);

        return ResponseEntity.ok(documento);
    }

    // Obtiene los documentos del usuario
    @GetMapping
    public ResponseEntity<List<Documento>> obtenerDocumentos(Authentication auth) {
        User usuario = usuarioRepositorio.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Documento> documentos = documentoRepositorio.findByUsuarioId(usuario.getId());
        return ResponseEntity.ok(documentos);
    }
}