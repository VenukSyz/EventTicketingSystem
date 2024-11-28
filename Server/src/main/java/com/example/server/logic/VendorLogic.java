package com.example.server.logic;

import com.example.server.dto.ConfigurationDTO;
import com.example.server.service.LogBroadcaster;

public class VendorLogic extends UserLogic{
    private static long vendorCounter;
    private final int ticketsPerRelease;
    private final int releaseIntervalMilliseconds;
    private final LogBroadcaster logBroadcaster;


    public VendorLogic(LogBroadcaster logBroadcaster,TicketPoolLogic ticketPool, ConfigurationDTO configuration, String name, String email, String phoneNum) {
        super(++vendorCounter, ticketPool, name, email, phoneNum);
        this.logBroadcaster = logBroadcaster;
        ticketsPerRelease = configuration.getTicketsPerRelease();
        releaseIntervalMilliseconds = configuration.getReleaseIntervalMilliseconds();
    }

    @Override
    public void run() {
        try {
            while (true) {
                boolean ticketsAdded = this.getTicketPool().addTickets(ticketsPerRelease, this.getName());
                if (!ticketsAdded) {
                    synchronized (logBroadcaster) {
                        logBroadcaster.log(this.getName() + " is stopping as all tickets have been added.");
                    }
                    break;
                }

                Thread.sleep(releaseIntervalMilliseconds);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            synchronized (logBroadcaster) {
                logBroadcaster.log(this.getName() + " interrupted");
            }
        }
    }
}

