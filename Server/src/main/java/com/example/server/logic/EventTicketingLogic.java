package com.example.server.logic;

import com.example.server.dto.ConfigurationDTO;
import com.example.server.service.LogBroadcaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventTicketingLogic {
    private final TicketPoolLogic ticketPool;
    private final ConfigurationDTO configuration;
    private final List<Thread> vendorThreads = Collections.synchronizedList(new ArrayList<>());
    private final List<Thread> customerThreads = Collections.synchronizedList(new ArrayList<>());
    private final LogBroadcaster logBroadcaster;
    private boolean running = false;  // System status flag
    private int numVendors;
    private int numCustomers;

    public EventTicketingLogic(LogBroadcaster logBroadcaster, ConfigurationDTO configuration, int numVendors, int numCustomers) {
        this.logBroadcaster = logBroadcaster;
        // Initialize TicketPool with configuration values
        this.configuration = configuration;
        ticketPool = new TicketPoolLogic(this.logBroadcaster, this.configuration);

        // Ask for the number of vendors and customers
        this.numVendors = numVendors;
        this.numCustomers = numCustomers;

        // Create vendors and customers based on input
        createVendors(numVendors);
        createCustomers(numCustomers);
    }

    private void createVendors(int numVendors) {
        for (int i = 0; i < numVendors; i++) {
            VendorLogic vendor = new VendorLogic(logBroadcaster, ticketPool, configuration,"Vendor-" + (i + 1), "vendor" + (i + 1) + "@example.com", "1234567890");
            Thread vendorThread = new Thread(vendor, "VendorThread-" + (i + 1));
            vendorThreads.add(vendorThread);
        }
    }

    private void createCustomers(int numCustomers) {
        for (int i = 0; i < numCustomers; i++) {
            CustomerLogic customer = new CustomerLogic(logBroadcaster, ticketPool, configuration,"Customer-" + (i + 1), "customer" + (i + 1) + "@example.com", "0987654321");
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

