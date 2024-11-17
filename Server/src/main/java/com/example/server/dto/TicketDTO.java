package com.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketDTO {
    private Long ticketId;
    private Double price;
    private String status;  // Using a String to represent TicketStatus or you can use the enum directly
    private Long eventId;   // Only the event ID instead of the full Event object
    private Long transactionId;  // Only the transaction ID instead of the full Transaction object
    private LocalDateTime releaseTime;
}
