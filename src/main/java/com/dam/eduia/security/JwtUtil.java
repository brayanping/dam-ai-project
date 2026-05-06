package com.dam.eduia.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secreto;

    @Value("${jwt.expiration}")
    private Long expiracion;

    // Genera un token JWT para el usuario
    public String generarToken(String email) {
        Key clave = Keys.hmacShaKeyFor(secreto.getBytes());
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiracion))
                .signWith(clave)
                .compact();
    }

    // Obtiene el email del token
    public String obtenerEmail(String token) {
        Key clave = Keys.hmacShaKeyFor(secreto.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(clave)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Valida si el token es correcto
    public boolean validarToken(String token) {
        try {
            Key clave = Keys.hmacShaKeyFor(secreto.getBytes());
            Jwts.parserBuilder().setSigningKey(clave).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}