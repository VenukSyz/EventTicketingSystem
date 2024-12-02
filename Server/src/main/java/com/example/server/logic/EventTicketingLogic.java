package com.example.server.logic;

import com.example.server.dto.ConfigurationDTO;
import com.example.server.service.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventTicketingLogic {
    private final TicketPoolLogic ticketPool;
    private final ConfigurationDTO configuration;
    private final List<Thread> vendorThreads;
    private final List<Thread> customerThreads;
    private final LogBroadcaster logBroadcaster;
    private final int numVendors;
    private final int numCustomers;
    private boolean running = false;  // System status flag

    public EventTicketingLogic(LogBroadcaster logBroadcaster, ConfigurationDTO configuration, int numVendors, int numCustomers) {
        this.logBroadcaster = logBroadcaster;
        this.vendorThreads = Collections.synchronizedList(new ArrayList<>());
        this.customerThreads = Collections.synchronizedList(new ArrayList<>());

        // Initialize TicketPool with configuration values
        this.configuration = configuration;
        ticketPool = new TicketPoolLogic(this.logBroadcaster, this.configuration);

        this.numVendors = numVendors;
        this.numCustomers = numCustomers;

        // Create vendors and customers based on input
        createVendors(numVendors);
        createCustomers(numCustomers);
    }

    private void createVendors(int numVendors) {
        for (int i = 0; i < numVendors; i++) {
            VendorLogic vendor = new VendorLogic(logBroadcaster, ticketPool, configuration, "Vendor-" + (i + 1),"vendor" + (i + 1) + "@example.com","123456789" + (i + 1));
            Thread vendorThread = new Thread(vendor, "VendorThread-" + (i + 1));
            vendorThreads.add(vendorThread);
        }
    }

    private void createCustomers(int numCustomers) {
        for (int i = 0; i < numCustomers; i++) {
            CustomerLogic customer = new CustomerLogic(logBroadcaster, ticketPool, configuration, "Customer-" + (i + 1),"Customer-" + (i + 1) + "@example.com","123456789" + (i + 1));
            Thread customerThread = new Thread(customer, "CustomerThread-" + (i + 1));
            customerThreads.add(customerThread);
        }
    }

    public void start() {
        if (running) {
            logBroadcaster.log("System is already running.");
            return;
        }

        logBroadcaster.log("Starting the system...");
        running = true;

        // Check if vendor and customer threads are empty; if so, recreate them
        if (vendorThreads.isEmpty() || customerThreads.isEmpty()) {
            createVendors(numVendors);
            createCustomers(numCustomers);
        }

        // Start all vendor and customer threads
        synchronized (vendorThreads) {
            for (Thread vendorThread : vendorThreads) {
                vendorThread.start();
            }
        }
        synchronized (customerThreads) {
            for (Thread customerThread : customerThreads) {
                customerThread.start();
            }
        }
    }

    public void stop() {
        if (!running) {
            logBroadcaster.log("System is not running.");
            return;
        }

        logBroadcaster.log("Stopping the system...");
        running = false;

        // Interrupt all vendor and customer threads
        synchronized (vendorThreads) {
            for (Thread vendorThread : vendorThreads) {
                vendorThread.interrupt();
            }
            vendorThreads.clear();  // Clear the list after interrupting all threads
        }
        synchronized (customerThreads) {
            for (Thread customerThread : customerThreads) {
                customerThread.interrupt();
            }
            customerThreads.clear();  // Clear the list after interrupting all threads
        }
    }

    public TicketPoolLogic getTicketPool() {
        return ticketPool;
    }
}

