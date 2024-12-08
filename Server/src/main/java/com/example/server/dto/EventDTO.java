package com.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventDTO {
    private long id;
    private String eventName;
    private float ticketPrice;
    private int maxCapacity;
    private int soldOutTickets;
}
