package com.example.server.logic;

import com.example.server.dto.ConfigurationDTO;
import com.example.server.service.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The main logic for managing the event ticketing system. This class handles the creation,
 * management, and interaction of customers, vendors, and the ticket pool.
 */
public class EventTicketingLogic {
    private final TicketPoolLogic ticketPool;
    private final ConfigurationDTO configuration;
    private final List<CustomerLogic> customers;
    private final List<VendorLogic> vendors;
    private final List<Thread> vendorThreads;
    private final List<Thread> customerThreads;
    private final LogBroadcaster logBroadcaster;
    private int numVendors;
    private int numCustomers;
    private boolean running = false;  // System status flag

    /**
     * Constructs the EventTicketingLogic with the specified parameters.
     *
     * @param logBroadcaster the log broadcaster for logging messages
     * @param configuration  the configuration for the system
     * @param numVendors     the number of vendors
     * @param numCustomers   the number of customers
     */
    public EventTicketingLogic(LogBroadcaster logBroadcaster, ConfigurationDTO configuration, int numVendors, int numCustomers) {
        this.logBroadcaster = logBroadcaster;
        this.customers = new ArrayList<>();
        this.vendors = new ArrayList<>();
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

    /**
     * Creates the specified number of vendor threads and initializes their logic.
     *
     * @param numVendors the number of vendors to create
     */
    private void createVendors(int numVendors) {
        if (vendors.isEmpty()) {
            for (int i = 0; i < numVendors; i++) {
                VendorLogic vendor = new VendorLogic(logBroadcaster, ticketPool, configuration,"Vendor-" + (i + 1), "vendor" + (i + 1) + "@example.com", "1234567890");
                vendors.add(vendor);
                Thread vendorThread = new Thread(vendor, "VendorThread-" + (i + 1));
                vendorThreads.add(vendorThread);
            }
        } else {
            for (int i = 0; i < numVendors; i++) {
                VendorLogic vendor = vendors.get(i);
                Thread vendorThread = new Thread(vendor, "VendorThread-" + (i + 1));
                vendorThreads.add(vendorThread);
            }
        }
    }

    /**
     * Updates the number of vendor threads in the system based on the new vendor count.
     * If there are more vendors than the current count, new vendors are created. If there are fewer,
     * the excess vendors are removed.
     *
     * @param numVendors the new number of vendors to be managed by the system
     */
    private void updateVendor(int numVendors) {
        if (numVendors == this.numVendors) {
            return;
        } else if (numVendors > this.numVendors) {
            for (int i = this.numVendors; i < numVendors; i++) {
                VendorLogic vendor = new VendorLogic(logBroadcaster, ticketPool, configuration,"Vendor-" + (i + 1), "vendor" + (i + 1) + "@example.com", "1234567890");
                vendors.add(vendor);
            }
        } else {
            for (int i = this.numVendors; i > numVendors; i--) {
                vendors.remove(i - 1);
            }
        }
        this.numVendors = numVendors;
    }

    /**
     * Creates the specified number of customer threads and initializes their logic.
     *
     * @param numCustomers the number of customers to create
     */
    private void createCustomers(int numCustomers) {
        if (customers.isEmpty()) {
            for (int i = 0; i < numCustomers; i++) {
                CustomerLogic customer = new CustomerLogic(logBroadcaster, ticketPool, configuration, "Customer-" + (i + 1), "customer" + (i + 1) + "@example.com", "0987654321");
                customers.add(customer);
                Thread customerThread = new Thread(customer, "CustomerThread-" + (i + 1));
                customerThreads.add(customerThread);
            }
        } else {
            for (int i = 0; i < numCustomers; i++) {
                CustomerLogic customer = customers.get(i);
                Thread customerThread = new Thread(customer, "CustomerThread-" + (i + 1));
                customerThreads.add(customerThread);
            }
        }
    }

    /**
     * Updates the number of customer threads in the system based on the new customer count.
     * If there are more customers than the current count, new customers are created. If there are fewer,
     * the excess customers are removed.
     *
     * @param numCustomers the new number of customers to be managed by the system
     */
    private void updateCustomers(int numCustomers) {
        if (numCustomers == this.numCustomers) {
            return;
        } else if (numCustomers > this.numCustomers) {
            for (int i = this.numCustomers; i < numCustomers; i++) {
                CustomerLogic customer = new CustomerLogic(logBroadcaster, ticketPool, configuration, "Customer-" + (i + 1), "customer" + (i + 1) + "@example.com", "0987654321");
                customers.add(customer);
            }
        } else {
            for (int i = this.numCustomers; i > numCustomers; i--) {
                customers.remove(i - 1);
            }
        }
        this.numCustomers = numCustomers;
    }

    /**
     * Starts the event ticketing system by launching vendor and customer threads.
     */
    public void start(boolean resumed) {
        if (running) {
            logBroadcaster.log("System is already running.");
            return;
        }

        logBroadcaster.log(resumed ? "Resuming the system..." : "Starting the system...");
        running = true;

        // Check if vendor and customer threads are empty; if so, recreate them
        if (vendorThreads.isEmpty() && customerThreads.isEmpty()) {
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

    /**
     * Resumes the system by updating the number of vendors and customers, and then starting the system.
     * This method allows the system to resume with a new configuration of vendors and customers.
     *
     * @param numVendors   the new number of vendors to be used in the system
     * @param numCustomers the new number of customers to be used in the system
     */
    public void resume(int numVendors, int numCustomers) {
        updateVendor(numVendors);
        updateCustomers(numCustomers);
        start(true);
    }

    /**
     * Stops the event ticketing system by interrupting vendor and customer threads.
     */
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

    /**
     * Retrieves the TicketPoolLogic instance.
     *
     * @return the ticket pool logic
     */
    public TicketPoolLogic getTicketPool() {
        return ticketPool;
    }
}