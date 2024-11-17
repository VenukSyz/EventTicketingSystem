package com.example.server;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@org.springframework.context.annotation.Configuration
@ConfigurationProperties(prefix = "configuration")
@Data
public class Configuration {

    private int maxTicketCapacity;
    private int totalTickets;

    private int releaseInterval;
    private int ticketsPerRelease;

    private int purchaseInterval;
    private int ticketsPerPurchase;
}
