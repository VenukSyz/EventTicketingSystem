import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class EventTicketingSystem {
    private final TicketPool ticketPool;
    private final List<Thread> vendorThreads = Collections.synchronizedList(new ArrayList<>());
    private final List<Thread> customerThreads = Collections.synchronizedList(new ArrayList<>());
    private boolean running = false;  // System status flag
    int numVendors;
    int numCustomers;

    public EventTicketingSystem() {
        // Load or initialize configuration
        System.out.println("=== Real-Time Event Ticketing System ===");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Load configuration from file? (yes/no): ");
        String loadFromFile = scanner.nextLine().trim().toLowerCase();

        if (loadFromFile.equals("yes")) {
            Configuration.loadFromFile();
        } else {
            Configuration.initialize();
            // Prompt to save configuration to a file
            while (true){
                System.out.print("Would you like to save the current configuration to a file? (yes/no): ");
                String saveToFile = scanner.nextLine().trim().toLowerCase();
                if (saveToFile.equalsIgnoreCase("yes")) {
                    Configuration.saveToFile();
                    break;
                } else if (saveToFile.equalsIgnoreCase("no")) {
                    break;
                } else {
                    System.out.println("Invalid input! Try again.\n");
                }

            }
        }

        // Initialize TicketPool with configuration values
        ticketPool = new TicketPool();

        // Ask for the number of vendors and customers
        numVendors = getPositiveIntegerInput("Enter the number of vendors: ");
        numCustomers = getPositiveIntegerInput("Enter the number of customers: ");

        // Create vendors and customers based on input
        createVendors(numVendors);
        createCustomers(numCustomers);
    }

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

    private void createVendors(int numVendors) {
        for (int i = 0; i < numVendors; i++) {
            Vendor vendor = new Vendor(ticketPool, "Vendor-" + (i + 1), "vendor" + (i + 1) + "@example.com", "1234567890");
            Thread vendorThread = new Thread(vendor, "VendorThread-" + (i + 1));
            vendorThreads.add(vendorThread);
        }
    }

    private void createCustomers(int numCustomers) {
        for (int i = 0; i < numCustomers; i++) {
            Customer customer = new Customer(ticketPool, "Customer-" + (i + 1), "customer" + (i + 1) + "@example.com", "0987654321");
            Thread customerThread = new Thread(customer, "CustomerThread-" + (i + 1));
            customerThreads.add(customerThread);
        }
    }

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

    public void checkStatus() {
        System.out.println("=== System Status ===");
        System.out.println("Running: " + running);
        System.out.println("Vendors: " + numVendors);
        System.out.println("Customers: " + numCustomers);
        System.out.println("Tickets in pool: " + ticketPool.getAvailableTickets());
        System.out.println("Sold out Tickets: " + ticketPool.getSoldOutTickets());
    }

    public static void main(String[] args) {
        EventTicketingSystem system = new EventTicketingSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nEnter a command (start, stop, status, exit): ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "start":
                    system.start();
                    break;
                case "stop":
                    system.stop();
                    break;
                case "status":
                    system.checkStatus();
                    break;
                case "exit":
                    system.stop();
                    System.out.println("Exiting the system.");
                    return;
                default:
                    System.out.println("Unknown command. Please enter 'start', 'stop', 'status', or 'exit'.");
            }
        }
    }
}

