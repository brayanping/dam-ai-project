package com.dam.eduia.controller;

import com.dam.eduia.dto.LoginDTO;
import com.dam.eduia.dto.RegistroDTO;
import com.dam.eduia.dto.RespuestaAuthDTO;
import com.dam.eduia.service.ServicioAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ControladorAuth {

    @Autowired
    private ServicioAuth servicioAuth;

    @PostMapping("/registro")
    public ResponseEntity<RespuestaAuthDTO> registro(@RequestBody RegistroDTO dto) {
        RespuestaAuthDTO respuesta = servicioAuth.registrar(dto);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaAuthDTO> login(@RequestBody LoginDTO dto) {
        RespuestaAuthDTO respuesta = servicioAuth.login(dto);
        return ResponseEntity.ok(respuesta);
    }
}