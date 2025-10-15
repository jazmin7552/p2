package com.example.demo.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComandaNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    // Enviar notificación general (a todos los clientes)
    public void notificarNuevaComanda(String mensaje) {
        messagingTemplate.convertAndSend("/topic/comandas/nueva", mensaje);
    }

    // Enviar notificación específica (por ejemplo, cambio de estado)
    public void notificarCambioEstado(String mensaje) {
        messagingTemplate.convertAndSend("/topic/comandas/estado", mensaje);
    }
}
