package com.supernovaworks.ubicacion.config;

import com.supernovaworks.ubicacion.handler.UbicacionWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final UbicacionWebSocketHandler ubicacionWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(ubicacionWebSocketHandler, "/ws/ubicacion")
                .setAllowedOrigins("*"); // en prod puedes restringir esto
    }
}
