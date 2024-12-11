package com.example.server.logic;

import com.example.server.dto.ConfigurationDTO;
import com.example.server.service.LogBroadcaster;

/**
 * Represents a customer in the system who retrieves tickets from the ticket pool.
 * Extends {@link UserLogic} and implements a runnable behavior to operate in a multi-threaded environment.
 */
public class CustomerLogic extends UserLogic{
    private static long customerCounter = 0;
    private final int ticketsPerRetrieval;
    private final int retrievalIntervalMilliseconds;

    /**
     * Constructs a CustomerLogic instance with the specified configuration and user details.
     *
     * @param logBroadcaster the log broadcaster for logging activities
     * @param ticketPool     the ticket pool from which tickets are retrieved
     * @param configuration  the configuration settings for ticket retrieval
     * @param name           the name of the customer
     * @param email          the email address of the customer
     * @param phoneNum       the phone number of the customer
     */
    public CustomerLogic(LogBroadcaster logBroadcaster, TicketPoolLogic ticketPool, ConfigurationDTO configuration, String name, String email, String phoneNum) {
        super(++customerCounter, logBroadcaster,ticketPool, name, email, phoneNum);
        this.ticketsPerRetrieval = configuration.getTicketsPerRetrieval();
        this.retrievalIntervalMilliseconds = configuration.getRetrievalIntervalMilliseconds();
    }

    /**
     * @inheritDoc
     * Executes the customer's ticket retrieval process in a loop.
     * The customer retrieves tickets at regular intervals until there are no enough tickets left in the pool.
     */
    @Override
    public void run() {
        try {
            while (true) {
                synchronized (this.getTicketPool()) {
                    if (this.getTicketPool().getToBeSoldOutTickets() < this.ticketsPerRetrieval) {
                        synchronized (this.getLogBroadcaster()) {
                            this.getLogBroadcaster().log(this.getName() + " stopping: Not enough tickets left to retrieve.");
                        }
                        break;
                    }
                }
                boolean success = this.getTicketPool().removeTickets(ticketsPerRetrieval, this.getName());

                if (!success) {
                    synchronized (this.getLogBroadcaster()) {
                        this.getLogBroadcaster().log(this.getName() + " could not retrieve tickets");
                    }
                }

                Thread.sleep(retrievalIntervalMilliseconds);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            synchronized (this.getLogBroadcaster()) {
                this.getLogBroadcaster().log(this.getName() + " interrupted.");
            }
        }
    }
}
