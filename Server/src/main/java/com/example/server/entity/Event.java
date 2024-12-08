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

