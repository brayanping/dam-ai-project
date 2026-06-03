package com.dam.eduia.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.http.MediaType;
import java.util.Map;

@Service
public class ServicioIACliente {

    private final WebClient webClient;

    public ServicioIACliente() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8000")
                .build();
    }

    public String hacerPregunta(String pregunta) {
        try {
            Map respuesta = webClient.post()
                    .uri("/ia/preguntar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("pregunta", pregunta))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return respuesta != null ? (String) respuesta.get("respuesta") : "Sin respuesta";

        } catch (WebClientRequestException e) {
            // El servicio Python no está arrancado
            System.out.println("❌ Servicio IA no disponible: " + e.getMessage());
            return "El servicio de IA no está disponible en este momento. Por favor, inténtalo más tarde.";

        } catch (Exception e) {
            // Cualquier otro error
            System.out.println("❌ Error en servicio IA: " + e.getMessage());
            return "Error al procesar tu pregunta. Por favor, inténtalo de nuevo.";
        }
    }

    public void procesarDocumento(String rutaArchivo, String tipo) {
        try {
            webClient.post()
                    .uri("/ia/procesar-documento")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("ruta_archivo", rutaArchivo, "tipo", tipo))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

        } catch (WebClientRequestException e) {
            System.out.println("❌ Servicio IA no disponible para procesar documento: " + e.getMessage());
            throw new RuntimeException("El servicio de IA no está disponible. El documento no pudo procesarse.");

        } catch (Exception e) {
            System.out.println("❌ Error procesando documento: " + e.getMessage());
            throw new RuntimeException("Error al procesar el documento: " + e.getMessage());
        }
    }
}