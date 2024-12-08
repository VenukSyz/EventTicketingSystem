import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the Real-Time Event Ticketing System.
 * This system manages vendors and customers interacting with a shared {@link TicketPool}.
 * Vendors release tickets into the pool, and customers retrieve them in a synchronized multithreaded environment.
 */
public class EventTicketingSystem {
    /** The shared pool of tickets for the system. */
    private TicketPool ticketPool;
    /** List of threads representing vendors in the system. */
    private final List<Thread> vendorThreads = Collections.synchronizedList(new ArrayList<>());
    /** List of threads representing customers in the system. */
    private final List<Thread> customerThreads = Collections.synchronizedList(new ArrayList<>());
    /** Flag indicating whether the system is running. */
    private boolean running = false;
    /** Number of vendor threads in the system. */
    private int numVendors;
    /** Number of customer threads in the system. */
    private int numCustomers;

    /**
     * Constructs the Event Ticketing System and initializes its configuration and components.
     */
    public EventTicketingSystem() {
        // Load or initialize configuration
        System.out.println("=== Real-Time Event Ticketing System ===");
        // Initialize TicketPool with configuration values
        initializeSystem();
    }

    /**
     * Initializes the system by configuring settings, creating the ticket pool, and setting up vendors and customers.
     */
    private void initializeSystem() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Load configuration from file or Enter 'No' to initialize a config manually? (yes/no): ");
        String loadFromFile = scanner.nextLine().trim().toLowerCase();

        if (loadFromFile.equals("yes")) {
            Configuration.loadFromFile();
        } else {
            Configuration.initialize();
            while (true) {
                System.out.print("Would you like to save the current configuration to a file? (yes/no): ");
                String saveToFile = scanner.nextLine().trim().toLowerCase();
                if (saveToFile.equals("yes")) {
                    Configuration.saveToFile();
                    break;
                } else if (saveToFile.equals("no")) {
                    break;
                } else {
                    System.out.println("Invalid input! Try again.\n");
                }
            }
        }

        ticketPool = new TicketPool();

        numVendors = getPositiveIntegerInput("Enter the number of vendors: ");
        numCustomers = getPositiveIntegerInput("Enter the number of customers: ");

        createVendors(numVendors);
        createCustomers(numCustomers);
    }

    /**
     * Prompts the user to input a positive integer.
     *
     * @param prompt the message displayed to the user for input.
     * @return a positive integer input by the user.
     */
    private int getPositiveIntegerInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        int number = 0;
        while (number <= 0) {
            System.out.print(prompt);
            try {
                number = Integer.parseInt(scanner.nextLine());
                if (number <= 0) {
                    System.out.println("Please enter a positive integer.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid positive integer.\n");
            }
        }
        return number;
    }

    /**
     * Creates the specified number of vendor threads and adds them to the vendor thread list.
     *
     * @param numVendors the number of vendors to create.
     */
    private void createVendors(int numVendors) {
        for (int i = 0; i < numVendors; i++) {
            Vendor vendor = new Vendor(ticketPool, "Vendor-" + (i + 1), "vendor" + (i + 1) + "@example.com", "1234567890");
            Thread vendorThread = new Thread(vendor, "VendorThread-" + (i + 1));
            vendorThreads.add(vendorThread);
        }
    }

    /**
     * Creates the specified number of customer threads and adds them to the customer thread list.
     *
     * @param numCustomers the number of customers to create.
     */
    private void createCustomers(int numCustomers) {
        for (int i = 0; i < numCustomers; i++) {
            Customer customer = new Customer(ticketPool, "Customer-" + (i + 1), "customer" + (i + 1) + "@example.com", "0987654321");
            Thread customerThread = new Thread(customer, "CustomerThread-" + (i + 1));
            customerThreads.add(customerThread);
        }
    }

    /**
     * Starts the system by running all vendor and customer threads.
     */
    public void start() {
        if (running) {
            System.out.println("System is already running.");
            return;
        }

        System.out.println("Starting the system...");
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

    /**
     * Stops the system by interrupting all vendor and customer threads.
     */
    public void stop() {
        if (!running) {
            System.out.println("System is not running.");
            return;
        }

        System.out.println("Stopping the system...");
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
     * Resets the system by reinitializing all configurations and components.
     */
    public void reset() {
        if (running) {
            System.out.println("System must be stopped before resetting.");
            return;
        }

        System.out.println("Resetting the system...");
        initializeSystem();
    }

    /**
     * Displays the current status of the system, including running state,
     * number of vendors and customers, and ticket pool statistics.
     */
    public void checkStatus() {
        System.out.println("=== System Status ===");
        System.out.println("Running: " + running);
        System.out.println("Vendors: " + numVendors);
        System.out.println("Customers: " + numCustomers);
        System.out.println("Tickets in pool: " + ticketPool.getAvailableTicketsInPool());
        System.out.println("Sold out Tickets: " + ticketPool.getSoldOutTickets());
        System.out.println("Total Tickets to be sold out: " + ticketPool.getToBeSoldOutTickets());
    }

    /**
     * Main entry point of the application. Allows the user to interact with the system
     * via a command-line interface.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        EventTicketingSystem system = new EventTicketingSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nEnter a command (start, stop, status, reset, exit): ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "start":
                    system.start();
                    break;
                case "stop":
                    system.stop();
                    break;
                case "reset":
                    system.reset();
                    break;
                case "status":
                    system.checkStatus();
                    break;
                case "exit":
                    system.stop();
                    System.out.println("Exiting the system.");
                    return;
                default:
                    System.out.println("Unknown command. Please enter 'start', 'stop', 'status', 'reset', or 'exit'.");
            }
        }
    }
}

