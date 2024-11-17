package com.example.server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @NonNull
    @Column(nullable = false)
    private String eventName;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime eventDate;

    @NonNull
    @Column(nullable = false)
    private String location;

    @NonNull
    @Column(nullable = false)
    private int maxTicketCapacity;

    @NonNull
    @Column(nullable = false)
    private int totalTicketsAvailable;

    @ManyToOne
    @JoinColumn(name = "vendorId", nullable = false )
    @NonNull
    private Vendor vendor;
}
