package com.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for control panel settings in the event ticketing system.
 * This class holds the necessary data for configuring and managing the event system from the control panel.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControlPanelDTO {
    private long configId;
    private String eventName;
    private float ticketPrice;
    private int noOfVendors;
    private int noOfCustomers;
}
