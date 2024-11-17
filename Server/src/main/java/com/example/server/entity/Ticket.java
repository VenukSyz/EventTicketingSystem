package com.example.server.entity;

import com.example.server.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @Column(nullable = false)
    @NonNull
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name = "eventId", nullable = false)
    @NonNull
    private Event event;

    @ManyToOne
    @JoinColumn(name = "transactionId")
    private TransactionRecord transactionRecord;

    @Column(nullable = false)
    @NonNull
    private LocalDateTime releaseTime;
}
