package com.dam.eduia.repository;

import com.dam.eduia.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentoRepositorio extends JpaRepository<Documento, Long> {
    List<Documento> findByUsuarioId(Long usuarioId);
    List<Documento> findByProcesado(boolean procesado);
}