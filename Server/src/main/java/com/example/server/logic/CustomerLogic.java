package com.example.server.logic;

import com.example.server.dto.ConfigurationDTO;
import com.example.server.service.LogBroadcaster;

public class CustomerLogic extends UserLogic{
    private static long customerCounter = 0;
    private final int ticketsPerRetrieval;
    private final int retrievalIntervalMilliseconds;
    private final LogBroadcaster logBroadcaster;

    public CustomerLogic(LogBroadcaster logBroadcaster, TicketPoolLogic ticketPool, ConfigurationDTO configuration, String name, String email, String phoneNum) {
        super(++customerCounter, ticketPool, name, email, phoneNum);
        this.logBroadcaster = logBroadcaster;
        this.ticketsPerRetrieval = configuration.getTicketsPerRetrieval();
        this.retrievalIntervalMilliseconds = configuration.getRetrievalIntervalMilliseconds();
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (this.getTicketPool()) {
                    if (this.getTicketPool().getToBeSoldOutTickets() < this.ticketsPerRetrieval) {
                        synchronized (logBroadcaster) {
                            logBroadcaster.log(this.getName() + " stopping: Not enough tickets left to retrieve.");
                        }
                        break;
                    }
                }
                boolean success = this.getTicketPool().removeTickets(ticketsPerRetrieval, this.getName());

                if (!success) {
                    synchronized (logBroadcaster) {
                        logBroadcaster.log(this.getName() + " could not retrieve tickets");
                    }
                }

                Thread.sleep(retrievalIntervalMilliseconds);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            synchronized (logBroadcaster) {
                logBroadcaster.log(this.getName() + " interrupted.");
            }
        }
    }
}
