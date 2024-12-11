package com.example.server.logic;

import com.example.server.dto.ConfigurationDTO;
import com.example.server.service.LogBroadcaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles ticket pool operations such as adding and removing tickets.
 * This class manages the available tickets, tickets added by vendors, and sold-out tickets
 * while adhering to the constraints set by the system configuration.
 */
public class TicketPoolLogic {
    private final List<Integer> availableTicketsInPool;
    private final List<Integer> ticketsAddedByVendors;
    private final List<Integer> soldOutTickets;
    private final int maxCapacity;
    private final int maxTicketsToAdd;
    private int toBeSoldOutTickets;
    private int totalTickets;
    private final LogBroadcaster logBroadcaster;

    /**
     * Constructs the TicketPoolLogic with the specified parameters.
     *
     * @param logBroadcaster the log broadcaster for logging messages
     * @param configuration  the configuration containing initial ticket information and constraints
     */
    public TicketPoolLogic(LogBroadcaster logBroadcaster, ConfigurationDTO configuration) {
        super();
        this.logBroadcaster = logBroadcaster;
        this.availableTicketsInPool = Collections.synchronizedList(new ArrayList<>());
        this.ticketsAddedByVendors = Collections.synchronizedList(new ArrayList<>());
        this.soldOutTickets = Collections.synchronizedList(new ArrayList<>());
        this.maxCapacity = configuration.getMaxTicketCapacity();
        this.maxTicketsToAdd = maxCapacity - configuration.getTotalTickets();
        this.toBeSoldOutTickets = configuration.getMaxTicketCapacity();
        this.totalTickets = configuration.getTotalTickets();

        for (int i = 0; i < totalTickets; i++) {
            this.availableTicketsInPool.add(i + 1);
        }
    }

    /**
     * Adds tickets to the pool by a vendor.
     *
     * @param tickets the number of tickets to add
     * @param name    the name of the vendor attempting to add tickets
     * @return {@code true} if tickets were successfully added, {@code false} otherwise
     */
    public synchronized boolean addTickets(int tickets, String name) {
        if (ticketsAddedByVendors.size() == maxTicketsToAdd) {
            logBroadcaster.log("Cannot add " + tickets + " tickets by " + name + ". Vendor addition limit reached.");
            return false;
        }

        if (ticketsAddedByVendors.size() + tickets > maxTicketsToAdd) {
            String msg = "Cannot add " + tickets + " tickets by " + name;
            tickets = maxTicketsToAdd - ticketsAddedByVendors.size();
            logBroadcaster.log(msg + " but added " + tickets + " tickets.");
        }

        for (int i = 0; i < tickets; i++) {
            availableTicketsInPool.add(++totalTickets);
            ticketsAddedByVendors.add(ticketsAddedByVendors.size() + 1);
        }

        logBroadcaster.log(name + " added " + tickets + " tickets. Total tickets now: " + availableTicketsInPool.size() + " (Tickets added by vendors: " + ticketsAddedByVendors.size() + ")");
        notifyAll();
        return true;
    }

    /**
     * Removes tickets from the pool for a customer.
     * If there are not enough tickets available, the customer will wait until tickets are added.
     *
     * @param tickets the number of tickets to remove
     * @param name    the name of the customer attempting to remove tickets
     * @return {@code true} if tickets were successfully removed, {@code false} otherwise
     */
    public synchronized boolean removeTickets(int tickets, String name) {
        while(availableTicketsInPool.size() < tickets) {
            logBroadcaster.log("Not enough tickets available. "+ name + " is waiting...");
            try {
                wait();
                if (toBeSoldOutTickets < tickets) {
                    logBroadcaster.log("Not enough tickets remaining for any customers. Current tickets left: " + toBeSoldOutTickets);
                    return false;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logBroadcaster.log(name + " interrupted while waiting.");
                return false;
            }
        }

        for (int i = 0; i < tickets; i++) {
            soldOutTickets.add(availableTicketsInPool.get(0));
            availableTicketsInPool.remove(0);
        }

        toBeSoldOutTickets -= tickets;
        logBroadcaster.log(name + " bought " + tickets + " tickets. Tickets left: " + availableTicketsInPool.size());
        notifyAll();
        return true;
    }

    /**
     * Gets the number of available tickets in the pool.
     *
     * @return the number of available tickets
     */
    public int getAvailableTicketsInPool() {
        return availableTicketsInPool.size();
    }

    /**
     * Gets the number of tickets that have been sold out.
     *
     * @return the number of sold-out tickets
     */
    public int getSoldOutTickets() {
        return soldOutTickets.size();
    }

    /**
     * Gets the number of tickets remaining to be sold out.
     *
     * @return the number of tickets remaining to be sold out
     */
    public int getToBeSoldOutTickets() {
        return toBeSoldOutTickets;
    }

    /**
     * Gets the number of tickets added by vendors.
     *
     * @return the number of tickets added by vendors
     */
    public int getTicketsAddedByVendors() {
        return ticketsAddedByVendors.size();
    }
}

