package com.example.server.dto;

import com.example.server.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDTO {

    private Long transactionId;
    private Long customerId;  // Only customer ID instead of the full Customer object
    private List<Long> ticketIds;  // Only ticket IDs instead of full Ticket objects
    private LocalDateTime purchaseTime;
    private Double amount;
    private TransactionStatus status;  // Using the enum directly

}
