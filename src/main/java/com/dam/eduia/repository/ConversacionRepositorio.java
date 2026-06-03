package com.dam.eduia.repository;

import com.dam.eduia.model.Conversacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ConversacionRepositorio extends JpaRepository<Conversacion, Long> {
    List<Conversacion> findByUserId(Long userId);
     // Consulta no trivial 3: conversaciones con más mensajes
    @Query("SELECT c FROM Conversacion c WHERE SIZE(c.messages) >= :minMensajes ORDER BY SIZE(c.messages) DESC")
    List<Conversacion> findConversacionesActivasPorMensajes(int minMensajes);
}