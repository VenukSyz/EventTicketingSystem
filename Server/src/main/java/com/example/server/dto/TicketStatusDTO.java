package com.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketStatusDTO {
    private int ticketsInPool;
    private int soldOutTickets;
    private int toBeSoldOutTickets;
}
