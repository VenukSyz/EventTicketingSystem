package com.example.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Entity representing the configuration settings for the ticketing system.
 * This class stores the configuration details that determine the behavior of the system,
 * such as ticket capacity, release and retrieval intervals, and ticket management rules.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int maxTicketCapacity;

    @Column(nullable = false)
    private int totalTickets;

    @Column(nullable = false)
    private int ticketsPerRelease;

    @Column(nullable = false)
    private int releaseIntervalMilliseconds;

    @Column(nullable = false)
    private int ticketsPerRetrieval;

    @Column(nullable = false)
    private int retrievalIntervalMilliseconds;


}
