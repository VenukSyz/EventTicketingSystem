package com.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing the configuration for the ticketing system.
 * This class encapsulates the configuration settings required to manage the ticket pool,
 * ticket release, and retrieval for the event ticketing system.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfigurationDTO {
    private long id;
    private String name;
    private int maxTicketCapacity;
    private int totalTickets;
    private int ticketsPerRelease;
    private int releaseIntervalMilliseconds;
    private int ticketsPerRetrieval;
    private int retrievalIntervalMilliseconds;

}
