package com.example.server.entity;

import com.example.server.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TransactionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    @NonNull
    private Customer customer;

    @OneToMany(mappedBy = "transactionRecord", cascade = CascadeType.ALL)
    @NonNull
    private List<Ticket> tickets;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    @NonNull
    @Column(nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    private TransactionStatus status;
}
