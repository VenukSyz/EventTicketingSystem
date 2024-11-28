package com.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControlPanelDTO {
    private long id;
    private int noOfVendors;
    private int noOfCustomers;
}
