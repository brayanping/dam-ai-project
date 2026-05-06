package com.dam.eduia.repository;

import com.dam.eduia.model.Conversacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConversacionRepositorio extends JpaRepository<Conversacion, Long> {
    List<Conversacion> findByUserId(Long userId);
}