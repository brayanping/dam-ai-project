package com.dam.eduia.repository;

import com.dam.eduia.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MensajeRepositorio extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByConversacionId(Long conversacionId);
}