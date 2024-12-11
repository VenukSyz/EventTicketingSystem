package com.example.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Entity representing an event in the ticketing system.
 * This class stores the event details, such as the event name, ticket price, capacity, and sold-out ticket count.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private float ticketPrice;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    private int soldOutTickets;
}

