package com.example.server.service;

import com.example.server.dto.TicketStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The LogBroadcaster class is responsible for logging messages and sending them to clients
 * via WebSocket for real-time communication. It also handles sending status updates related
 * to ticket sales, including timestamped messages and ticket status information.
 *
 * The class uses a {@link SimpMessagingTemplate} to send logs and status updates to specific
 * WebSocket endpoints (/topic/logs and /topic/status) for real-time updates in a connected client.
 * The messages are prefixed with the current date and time in "yyyy-MM-dd HH:mm:ss" format.
 */
@Service
public class LogBroadcaster {
    private final DateTimeFormatter formatter;
    private String currentDateTime;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Constructs a LogBroadcaster with the specified messaging template for broadcasting logs.
     *
     * @param messagingTemplate the messaging template used to send logs and status updates
     */
    @Autowired
    public LogBroadcaster(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Logs a message to the console and sends it to the /topic/logs endpoint.
     * The log includes a timestamp of when the log was generated.
     *
     * @param message the log message to be broadcasted
     */
    public void log(String message) {
        System.out.println(message);
        currentDateTime = LocalDateTime.now().format(formatter);// Print to console
        messagingTemplate.convertAndSend("/topic/logs",currentDateTime + " - " + message);
    }

    /**
     * Sends a status update to the /topic/status endpoint.
     * The status update includes a timestamp and current ticket status.
     *
     * @param status the current ticket status to be sent as a status update
     */
    public void sendStatusUpdate(TicketStatusDTO status) {
        currentDateTime = LocalDateTime.now().format(formatter);
        messagingTemplate.convertAndSend("/topic/status",status);
    }
}
