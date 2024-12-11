package com.example.server.logic;

import com.example.server.dto.ConfigurationDTO;
import com.example.server.service.LogBroadcaster;

/**
 * Represents a vendor in the system who releases tickets into the ticket pool.
 * Extends {@link UserLogic} and implements a runnable behavior to operate in a multi-threaded environment.
 */
public class VendorLogic extends UserLogic{
    private static long vendorCounter;
    private final int ticketsPerRelease;
    private final int releaseIntervalMilliseconds;

    /**
     * Constructs a VendorLogic instance with the specified configuration and user details.
     *
     * @param logBroadcaster the log broadcaster for logging activities
     * @param ticketPool     the ticket pool to which tickets are released
     * @param configuration  the configuration settings for ticket release
     * @param name           the name of the vendor
     * @param email          the email address of the vendor
     * @param phoneNum       the phone number of the vendor
     */
    public VendorLogic(LogBroadcaster logBroadcaster, TicketPoolLogic ticketPool, ConfigurationDTO configuration, String name, String email, String phoneNum) {
        super(++vendorCounter, logBroadcaster, ticketPool, name, email, phoneNum);
        ticketsPerRelease = configuration.getTicketsPerRelease();
        releaseIntervalMilliseconds = configuration.getReleaseIntervalMilliseconds();
    }

    /**
     * @inheritDoc
     * Executes the vendor's ticket release process in a loop.
     * The vendor releases tickets at regular intervals until the ticket pool is full.
     */
    @Override
    public void run() {
        try {
            while (true) {
                boolean ticketsAdded = this.getTicketPool().addTickets(ticketsPerRelease, this.getName());
                if (!ticketsAdded) {
                    synchronized (this.getLogBroadcaster()) {
                        this.getLogBroadcaster().log(this.getName() + " is stopping as all tickets have been added.");
                    }
                    break;
                }

                Thread.sleep(releaseIntervalMilliseconds);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            synchronized (this.getLogBroadcaster()) {
                this.getLogBroadcaster().log(this.getName() + " interrupted");
            }
        }
    }
}

