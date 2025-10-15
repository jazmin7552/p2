package com.example.demo.infrastructure.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefijo para los destinos que pueden suscribirse los clientes
        config.enableSimpleBroker("/topic");
        // Prefijo para los mensajes que envían los clientes al servidor
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint al que se conectan los clientes (por ejemplo, el frontend)
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}
