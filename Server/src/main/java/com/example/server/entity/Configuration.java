package com.example.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
