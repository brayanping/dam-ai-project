package com.dam.eduia.controller;
import com.dam.eduia.repository.ConversacionRepositorio;

import com.dam.eduia.dto.PreguntaDTO;
import com.dam.eduia.dto.RespuestaDTO;
import com.dam.eduia.model.Conversacion;
import com.dam.eduia.model.Mensaje;
import com.dam.eduia.service.ServicioChat;
import com.dam.eduia.service.ServicioIACliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ControladorChat {
	@Autowired
	private ConversacionRepositorio conversacionRepositorio;
   

    @Autowired
    private ServicioChat servicioChat;

    @Autowired
    private ServicioIACliente servicioIACliente;

    @PostMapping("/conversacion")
    public ResponseEntity<Conversacion> nuevaConversacion(Authentication auth) {
        Conversacion conversacion = servicioChat.crearConversacion(auth.getName());
        return ResponseEntity.ok(conversacion);
    }

    @GetMapping("/conversaciones")
    public ResponseEntity<List<Conversacion>> obtenerConversaciones(Authentication auth) {
        List<Conversacion> conversaciones = servicioChat.obtenerConversaciones(auth.getName());
        return ResponseEntity.ok(conversaciones);
    }

    @GetMapping("/conversacion/{id}/mensajes")
    public ResponseEntity<List<Mensaje>> obtenerMensajes(@PathVariable Long id) {
        List<Mensaje> mensajes = servicioChat.obtenerMensajes(id);
        return ResponseEntity.ok(mensajes);
    }

    @PostMapping(value = "/preguntar", produces = "application/json")
    public ResponseEntity<RespuestaDTO> preguntar(
            @RequestBody PreguntaDTO dto,
            Authentication auth) {

        // Si no viene conversacionId crea una nueva conversación
        Long conversacionId = dto.getConversacionId();
        boolean esNuevaConversacion = false;
        
        if (conversacionId == null) {
            Conversacion nueva = servicioChat.crearConversacion(auth.getName());
            conversacionId = nueva.getId();
            esNuevaConversacion = true;
        }

        // Guarda la pregunta del usuario
        servicioChat.guardarMensaje(conversacionId, dto.getPregunta(), "usuario");
        
        // Si es una nueva conversación, actualiza el título basado en la primera pregunta
        if (esNuevaConversacion) {
            servicioChat.actualizarTituloDesdeFirstPregunta(conversacionId, dto.getPregunta());
        }

        // Envía la pregunta al servicio de IA
        String respuestaIA = servicioIACliente.hacerPregunta(dto.getPregunta());

        // Guarda la respuesta de la IA
        Mensaje mensajeRespuesta = servicioChat.guardarMensaje(
                conversacionId, respuestaIA, "asistente");

        return ResponseEntity.ok(new RespuestaDTO(
                respuestaIA, conversacionId, mensajeRespuesta.getId()));
    }
    @DeleteMapping("/conversacion/{id}")
    public ResponseEntity<Void> eliminarConversacion(@PathVariable Long id) {
        conversacionRepositorio.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "/conversacion/{id}/exportar", produces = "application/json")
public ResponseEntity<Map<String, Object>> exportarConversacion(
        @PathVariable Long id, Authentication auth) {
    
    List<Mensaje> mensajes = servicioChat.obtenerMensajes(id);
    Conversacion conversacion = conversacionRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Conversación no encontrada"));

    Map<String, Object> exportacion = new HashMap<>();
    exportacion.put("titulo", conversacion.getTitle());
    exportacion.put("fecha", conversacion.getCreatedAt());
    exportacion.put("usuario", auth.getName());
    exportacion.put("mensajes", mensajes.stream().map(m -> {
        Map<String, String> msg = new HashMap<>();
        msg.put("rol", m.getRol());
        msg.put("contenido", m.getContenido());
        return msg;
    }).toList());

    return ResponseEntity.ok(exportacion);
}
}