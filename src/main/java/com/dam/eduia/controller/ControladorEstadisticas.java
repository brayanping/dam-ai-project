package com.dam.eduia.controller;

import com.dam.eduia.model.User;
import com.dam.eduia.repository.ConversacionRepositorio;
import com.dam.eduia.repository.DocumentoRepositorio;
import com.dam.eduia.repository.MensajeRepositorio;
import com.dam.eduia.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/estadisticas")
public class ControladorEstadisticas {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ConversacionRepositorio conversacionRepositorio;

    @Autowired
    private MensajeRepositorio mensajeRepositorio;

    @Autowired
    private DocumentoRepositorio documentoRepositorio;

    // Estadísticas del usuario actual
    @GetMapping(value = "/mias", produces = "application/json")
    public ResponseEntity<Map<String, Object>> misEstadisticas(Authentication auth) {
        User usuario = usuarioRepositorio.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Map<String, Object> stats = new HashMap<>();
        stats.put("usuario", usuario.getUsername());
        stats.put("totalConversaciones", conversacionRepositorio.findByUserId(usuario.getId()).size());
        stats.put("totalDocumentos", documentoRepositorio.findByUsuarioId(usuario.getId()).size());
        stats.put("totalMensajes", mensajeRepositorio.count());

        return ResponseEntity.ok(stats);
    }

    // Estadísticas globales del sistema
    @GetMapping(value = "/globales", produces = "application/json")
    public ResponseEntity<Map<String, Object>> estadisticasGlobales() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsuarios", usuarioRepositorio.count());
        stats.put("totalConversaciones", conversacionRepositorio.count());
        stats.put("totalMensajes", mensajeRepositorio.count());
        stats.put("totalDocumentos", documentoRepositorio.count());
        stats.put("usuariosMasActivos", usuarioRepositorio.findUsuariosOrdenadosPorActividad());

        return ResponseEntity.ok(stats);
    }
}