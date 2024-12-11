package com.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing the current status of tickets in the pool.
 * This class holds the details related to the ticket availability, sales, and vendor contributions.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketStatusDTO {
    private int ticketsInPool;
    private int soldOutTickets;
    private int toBeSoldOutTickets;
    private int ticketsAddedByVendors;
    private float ticketSales;
}
