package com.supernovaworks.ubicacion.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernovaworks.ubicacion.dto.UbicacionDTO;
import com.supernovaworks.ubicacion.service.UbicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class UbicacionWebSocketHandler extends TextWebSocketHandler {

	private final UbicacionService ubicacionService;
	private final ObjectMapper objectMapper;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		System.out.println("🟢 Conexión establecida: " + session.getId() + " desde " + session.getRemoteAddress());
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		System.out.println("📩 Mensaje recibido: " + message.getPayload());
		try {
			UbicacionDTO ubicacion = objectMapper.readValue(message.getPayload(), UbicacionDTO.class);
			ubicacionService.procesarUbicacion(ubicacion);
		} catch (Exception e) {
			System.err.println("❗ Error al procesar mensaje: " + e.getMessage());
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) {
		System.err.println("❌ Error de transporte en sesión " + session.getId() + ": " + exception.getMessage());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		System.out.println("🔴 Conexión cerrada: " + session.getId() + " - " + status);
	}
}
