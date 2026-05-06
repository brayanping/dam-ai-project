package com.dam.eduia.service;

import com.dam.eduia.dto.LoginDTO;
import com.dam.eduia.dto.RegistroDTO;
import com.dam.eduia.dto.RespuestaAuthDTO;
import com.dam.eduia.model.User;
import com.dam.eduia.repository.UsuarioRepositorio;
import com.dam.eduia.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServicioAuth {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder encriptadorPassword;

    @Autowired
    private JwtUtil jwtUtil;

    // Registra un nuevo usuario
    public RespuestaAuthDTO registrar(RegistroDTO dto) {
        // Comprueba si el email ya existe
        if (usuarioRepositorio.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Comprueba si el username ya existe
        if (usuarioRepositorio.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        // Crea el nuevo usuario
        User usuario = new User();
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(encriptadorPassword.encode(dto.getPassword()));

        usuarioRepositorio.save(usuario);

        // Genera el token y devuelve la respuesta
        String token = jwtUtil.generarToken(usuario.getEmail());
        return new RespuestaAuthDTO(token, usuario.getUsername(), usuario.getEmail());
    }

    // Hace login de un usuario existente
    public RespuestaAuthDTO login(LoginDTO dto) {
        // Busca el usuario por email
        User usuario = usuarioRepositorio.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email o contraseña incorrectos"));

        // Comprueba la contraseña
        if (!encriptadorPassword.matches(dto.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Email o contraseña incorrectos");
        }

        // Genera el token y devuelve la respuesta
        String token = jwtUtil.generarToken(usuario.getEmail());
        return new RespuestaAuthDTO(token, usuario.getUsername(), usuario.getEmail());
    }
}