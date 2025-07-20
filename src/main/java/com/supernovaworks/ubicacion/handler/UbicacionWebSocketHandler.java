package com.supernovaworks.ubicacion.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernovaworks.ubicacion.dto.IdentificacionDTO;
import com.supernovaworks.ubicacion.dto.UbicacionDTO;
import com.supernovaworks.ubicacion.service.UbicacionService;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class UbicacionWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final UbicacionService ubicacionService;

    private static final Set<WebSocketSession> sesionesActivas = ConcurrentHashMap.newKeySet();

    private static final Map<String, Set<WebSocketSession>> viewersPorUserId = new ConcurrentHashMap<>();
    private static final Map<String, WebSocketSession> senders = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("🟢 Conexión establecida: " + session.getId() + " desde " + session.getRemoteAddress());
        sesionesActivas.add(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            JsonNode root = objectMapper.readTree(message.getPayload());
            String type = root.has("type") ? root.get("type").asText() : null;

            if ("viewer".equals(type)) {
                IdentificacionDTO dto = objectMapper.treeToValue(root, IdentificacionDTO.class);
                String aQuienVer = dto.getVerA();
                if (aQuienVer != null) {
                    viewersPorUserId
                        .computeIfAbsent(aQuienVer, k -> ConcurrentHashMap.newKeySet())
                        .add(session);
                    System.out.println("👁 Viewer " + dto.getUserId() + " quiere ver a " + aQuienVer);
                }
            } else if ("sender".equals(type)) {
                UbicacionDTO ubicacion = objectMapper.treeToValue(root, UbicacionDTO.class);
                System.out.println("📍 Ubicación recibida de " + ubicacion.getUserId() + ": " + ubicacion);
                ubicacionService.procesarUbicacion(ubicacion); // opcional
                String json = objectMapper.writeValueAsString(ubicacion);

                Set<WebSocketSession> subs = viewersPorUserId.getOrDefault(ubicacion.getUserId(), Set.of());
                for (WebSocketSession s : subs) {
                    if (s.isOpen()) {
                        s.sendMessage(new TextMessage(json));
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("❗ Error al procesar mensaje WebSocket: " + e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("🔴 Conexión cerrada: " + session.getId());
        sesionesActivas.remove(session);
        // Limpieza opcional de viewers
        viewersPorUserId.values().forEach(s -> s.remove(session));
    }
}

