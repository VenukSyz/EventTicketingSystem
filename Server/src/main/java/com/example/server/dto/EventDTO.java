package com.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventDTO{
    private Long eventId;
    private String eventName;
    private LocalDateTime eventDate;
    private String location;
    private int maxTicketCapacity;
    private int totalTicketCapacity;
    private Long vendorId;

}
