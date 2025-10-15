package com.example.demo.infrastructure.web.controller;

import com.example.demo.application.service.ComandaNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final ComandaNotificationService comandaNotificationService;

    //Endpoint que recibe mensajes del cliente y los reenvía a todos
    @MessageMapping("/comandas/nueva")
    @SendTo("/topic/comandas/nueva")
    public String enviarNotificacionComanda(String mensaje) {
        comandaNotificationService.notificarNuevaComanda(mensaje);
        return mensaje;
    }
}
