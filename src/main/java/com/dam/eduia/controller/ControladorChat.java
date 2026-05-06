package com.dam.eduia.controller;

import com.dam.eduia.dto.PreguntaDTO;
import com.dam.eduia.dto.RespuestaDTO;
import com.dam.eduia.model.Conversacion;
import com.dam.eduia.model.Mensaje;
import com.dam.eduia.service.ServicioChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ControladorChat {

    @Autowired
    private ServicioChat servicioChat;

    // Crea una nueva conversación
    @PostMapping("/conversacion")
    public ResponseEntity<Conversacion> nuevaConversacion(Authentication auth) {
        Conversacion conversacion = servicioChat.crearConversacion(auth.getName());
        return ResponseEntity.ok(conversacion);
    }

    // Obtiene todas las conversaciones del usuario
    @GetMapping("/conversaciones")
    public ResponseEntity<List<Conversacion>> obtenerConversaciones(Authentication auth) {
        List<Conversacion> conversaciones = servicioChat.obtenerConversaciones(auth.getName());
        return ResponseEntity.ok(conversaciones);
    }

    // Obtiene los mensajes de una conversación
    @GetMapping("/conversacion/{id}/mensajes")
    public ResponseEntity<List<Mensaje>> obtenerMensajes(@PathVariable Long id) {
        List<Mensaje> mensajes = servicioChat.obtenerMensajes(id);
        return ResponseEntity.ok(mensajes);
    }

    // Envía una pregunta a la IA
    @PostMapping("/preguntar")
    public ResponseEntity<RespuestaDTO> preguntar(
            @RequestBody PreguntaDTO dto,
            Authentication auth) {

        // Si no viene conversacionId, crea una nueva
        Long conversacionId = dto.getConversacionId();
        if (conversacionId == null) {
            Conversacion nueva = servicioChat.crearConversacion(auth.getName());
            conversacionId = nueva.getId();
        }

        // Guarda la pregunta del usuario
        servicioChat.guardarMensaje(conversacionId, dto.getPregunta(), "usuario");

        // Por ahora respuesta provisional hasta conectar con la IA
        String respuestaIA = "Respuesta de la IA pendiente de conectar";

        // Guarda la respuesta de la IA
        Mensaje mensajeRespuesta = servicioChat.guardarMensaje(
                conversacionId, respuestaIA, "asistente");

        return ResponseEntity.ok(new RespuestaDTO(
                respuestaIA, conversacionId, mensajeRespuesta.getId()));
    }
}