package com.example.server.service;

import com.example.server.dto.TicketStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class LogBroadcaster {
    private final DateTimeFormatter formatter;
    private String currentDateTime;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public LogBroadcaster(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public void log(String message) {
        System.out.println(message);
        currentDateTime = LocalDateTime.now().format(formatter);// Print to console
        messagingTemplate.convertAndSend("/topic/logs",currentDateTime + " - " + message);
    }

    public void sendStatusUpdate(TicketStatusDTO status) {
        currentDateTime = LocalDateTime.now().format(formatter);
        messagingTemplate.convertAndSend("/topic/status",status);
    }
}
