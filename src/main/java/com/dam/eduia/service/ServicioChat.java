package com.dam.eduia.service;

import com.dam.eduia.model.Conversacion;
import com.dam.eduia.model.Mensaje;
import com.dam.eduia.model.User;
import com.dam.eduia.repository.ConversacionRepositorio;
import com.dam.eduia.repository.MensajeRepositorio;
import com.dam.eduia.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioChat {

    @Autowired
    private ConversacionRepositorio conversacionRepositorio;

    @Autowired
    private MensajeRepositorio mensajeRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    // Crea una nueva conversación para el usuario
    public Conversacion crearConversacion(String email) {
        User usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Conversacion conversacion = new Conversacion();
        conversacion.setUser(usuario);
        return conversacionRepositorio.save(conversacion);
    }

    // Devuelve todas las conversaciones del usuario
    public List<Conversacion> obtenerConversaciones(String email) {
        User usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return conversacionRepositorio.findByUserId(usuario.getId());
    }

    // Guarda un mensaje en una conversación
    public Mensaje guardarMensaje(Long conversacionId, String contenido, String rol) {
        Conversacion conversacion = conversacionRepositorio.findById(conversacionId)
                .orElseThrow(() -> new RuntimeException("Conversación no encontrada"));

        Mensaje mensaje = new Mensaje();
        mensaje.setContenido(contenido);
        mensaje.setRol(rol);
        mensaje.setConversacion(conversacion);

        return mensajeRepositorio.save(mensaje);
    }

    // Devuelve todos los mensajes de una conversación
    public List<Mensaje> obtenerMensajes(Long conversacionId) {
        return mensajeRepositorio.findByConversacionId(conversacionId);
    }
}