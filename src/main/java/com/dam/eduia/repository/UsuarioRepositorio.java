package com.dam.eduia.repository;

import com.dam.eduia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface UsuarioRepositorio extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    // Consulta no trivial 1: usuarios con más de X conversaciones
    @Query("SELECT u FROM User u WHERE SIZE(u.conversations) > :minConversaciones")
    List<User> findUsuariosActivosPorConversaciones(int minConversaciones);

    // Consulta no trivial 2: usuarios ordenados por número de conversaciones
    @Query("SELECT u FROM User u ORDER BY SIZE(u.conversations) DESC")
    List<User> findUsuariosOrdenadosPorActividad();
}