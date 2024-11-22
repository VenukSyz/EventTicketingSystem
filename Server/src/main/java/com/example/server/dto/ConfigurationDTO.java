package com.example.server.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
