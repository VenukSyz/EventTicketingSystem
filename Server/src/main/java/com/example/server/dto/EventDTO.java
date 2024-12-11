package com.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing an event in the ticketing system.
 * This class holds the details of an event, including its name, ticket price,
 * maximum capacity, and the number of sold-out tickets.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventDTO {
    private long id;
    private String eventName;
    private float ticketPrice;
    private int maxCapacity;
    private int soldOutTickets;
}
