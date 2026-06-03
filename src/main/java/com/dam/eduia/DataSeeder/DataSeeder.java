package com.dam.eduia;

import com.dam.eduia.model.Conversacion;
import com.dam.eduia.model.Mensaje;
import com.dam.eduia.model.User;
import com.dam.eduia.repository.ConversacionRepositorio;
import com.dam.eduia.repository.MensajeRepositorio;
import com.dam.eduia.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ConversacionRepositorio conversacionRepositorio;

    @Autowired
    private MensajeRepositorio mensajeRepositorio;

    @Autowired
    private PasswordEncoder encriptadorPassword;

    @Override
    public void run(String... args) throws Exception {
        // Solo carga datos si no hay usuarios
        if (usuarioRepositorio.count() == 0) {
            cargarDatosEjemplo();
        }
    }

    private void cargarDatosEjemplo() {
        // Usuario 1
        User usuario1 = new User();
        usuario1.setUsername("alumno_dam");
        usuario1.setEmail("alumno@dam.com");
        usuario1.setPassword(encriptadorPassword.encode("123456"));
        usuarioRepositorio.save(usuario1);

        // Usuario 2
        User usuario2 = new User();
        usuario2.setUsername("profesor_dam");
        usuario2.setEmail("profesor@dam.com");
        usuario2.setPassword(encriptadorPassword.encode("123456"));
        usuarioRepositorio.save(usuario2);

        // Conversación de ejemplo para usuario1
        Conversacion conv1 = new Conversacion();
        conv1.setTitle("¿Qué es la herencia en Java?");
        conv1.setUser(usuario1);
        conversacionRepositorio.save(conv1);

        // Mensajes de ejemplo
        Mensaje pregunta1 = new Mensaje();
        pregunta1.setContenido("¿Qué es la herencia en Java?");
        pregunta1.setRol("usuario");
        pregunta1.setConversacion(conv1);
        mensajeRepositorio.save(pregunta1);

        Mensaje respuesta1 = new Mensaje();
        respuesta1.setContenido("La herencia en Java es un mecanismo que permite que una clase herede atributos y métodos de otra clase usando la palabra clave extends.");
        respuesta1.setRol("asistente");
        respuesta1.setConversacion(conv1);
        mensajeRepositorio.save(respuesta1);

        // Segunda conversación
        Conversacion conv2 = new Conversacion();
        conv2.setTitle("¿Qué es una API REST?");
        conv2.setUser(usuario1);
        conversacionRepositorio.save(conv2);

        Mensaje pregunta2 = new Mensaje();
        pregunta2.setContenido("¿Qué es una API REST?");
        pregunta2.setRol("usuario");
        pregunta2.setConversacion(conv2);
        mensajeRepositorio.save(pregunta2);

        Mensaje respuesta2 = new Mensaje();
        respuesta2.setContenido("Una API REST es una interfaz de programación que usa HTTP para comunicar sistemas usando los métodos GET, POST, PUT y DELETE.");
        respuesta2.setRol("asistente");
        respuesta2.setConversacion(conv2);
        mensajeRepositorio.save(respuesta2);

        System.out.println("✅ Datos de ejemplo cargados correctamente");
        System.out.println("   Usuario: alumno@dam.com / 123456");
        System.out.println("   Usuario: profesor@dam.com / 123456");
    }
}