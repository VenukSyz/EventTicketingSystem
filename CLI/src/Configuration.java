import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The Configuration class manages the configuration of a ticketing system,
 * including initialization, validation, saving, and loading of configuration data.
 */
public class Configuration {
    /** Maximum ticket capacity of the system. */
    private static int maxTicketCapacity;
    /** Total number of tickets initially available in the ticket pool. */
    private static int totalTickets;
    /** Number of tickets released by vendors at a time. */
    private static int ticketsPerRelease;
    /** Time interval (in milliseconds) between ticket releases. */
    private static int releaseIntervalMilliseconds;
    /** Number of tickets retrieved by customers at a time. */
    private static int ticketsPerRetrieval;
    /** Time interval (in milliseconds) between ticket retrievals. */
    private static int retrievalIntervalMilliseconds;

    /** Gson instance for JSON serialization and deserialization. */
    private static final Gson gson = new Gson();
    /** Scanner instance for user input. */
    private static final Scanner input = new Scanner(System.in);

    /**
     * Validates user input to ensure it is a positive integer.
     *
     * @param inputMessage the message displayed to the user for input.
     * @return a positive integer entered by the user.
     */
    private static int validateInput(String inputMessage) {
        while(true) {
            System.out.print(inputMessage);
            try {
                int value = input.nextInt();
                if (value <= 0) {
                    System.out.println("Please enter a positive number.\n");
                } else {
                    return value;
                }
            } catch (InputMismatchException e){
                System.out.println("Invalid Input! Please enter a positive number.\n");
                input.nextLine(); // To clear the invalid input
            }
        }
    }

    /**
     * Validates user input to ensure it is a positive integer within a specified limit.
     *
     * @param inputMessage the message displayed to the user for input.
     * @param maxLimit the maximum allowable value.
     * @return a positive integer within the specified limit.
     */
    private static int validateWithLimit(String inputMessage, int maxLimit) {
        while (true) {
            int value = validateInput(inputMessage);
            if (value <= maxLimit) {
                return value;
            } else {
                System.out.println("Value cannot exceed " + maxLimit + ". Please try again.\n");
            }
        }
    }

    /**
     * Initializes the configuration by prompting the user for input.
     */
    public static void initialize() {
        System.out.println("=== Initializing System Configuration ===");
        maxTicketCapacity = validateInput("Enter the max ticket capacity: ");
        totalTickets = validateWithLimit("Enter total number of tickets available in the pool: ", maxTicketCapacity - 1);
        ticketsPerRelease = validateWithLimit("Enter the number of tickets each vendor will release at a time: ", maxTicketCapacity - totalTickets);
        releaseIntervalMilliseconds = validateInput("Enter the release interval in milliseconds: ");
        ticketsPerRetrieval = validateWithLimit("Enter the number of tickets each customer will attempt to retrieve at a time: ", maxTicketCapacity);
        retrievalIntervalMilliseconds = validateInput("Enter the retrieval interval in milliseconds: ");
    }

    /**
     * Prompts the user to enter a file name and ensures it ends with ".json".
     *
     * @return a valid JSON file name.
     */
    private static String getFileName() {
        while(true) {
            System.out.print("Enter the file name: ");
            String fileName = input.nextLine();
            if (fileName.isEmpty()) {
                System.out.println("Can't leave the file name blank. Please try again.\n");
            } else {
                return fileName.endsWith(".json") ? fileName : fileName + ".json";
            }
        }
    }

    /**
     * Saves the current configuration to a JSON file.
     */
    public static void saveToFile() {
        String fileName = getFileName();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            gson.toJson(new ConfigurationData(), writer);
            System.out.println("Configuration saved to " + fileName + "\n");
        } catch (IOException e) {
            System.out.println("An error occurred while saving configuration: " + e.getMessage() + "\n");
        }
    }

    /**
     * Loads the configuration from a JSON file. If the file is missing or invalid,
     * the user is given options to re-enter the file name or configure settings manually.
     */
    public static void loadFromFile() {
        while (true) {
            String fileName;
            fileName = getFileName();
            while (true) {
                File file = new File(fileName);

                // Check if the file exists
                if (!file.exists()) {
                    System.out.println("File " + fileName + " does not exist.\n");
                    while (true) {
                        System.out.print("Enter '1' to re-enter the file name, or '2' to manually configure settings: ");
                        String choice = input.nextLine().trim();

                        if (choice.equals("2")) {
                            initialize();  // Prompt user to manually configure settings
                            return;
                        } else if (choice.equals("1")) {
                            fileName = getFileName();
                            break;
                        } else {
                            System.out.println("Invalid option. Please enter '1' or '2'.");
                        }
                    }
                } else{
                    break;
                }
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                ConfigurationData data = gson.fromJson(reader, ConfigurationData.class);
                if (data != null && isConfigurationDataValid(data)) {
                    maxTicketCapacity = data.maxTicketCapacity;
                    totalTickets = data.totalTickets;
                    ticketsPerRelease = data.ticketsPerRelease;
                    releaseIntervalMilliseconds = data.releaseIntervalMilliseconds;
                    ticketsPerRetrieval = data.ticketsPerRetrieval;
                    retrievalIntervalMilliseconds = data.retrievalIntervalMilliseconds;
                    System.out.println("Configuration loaded from " + fileName + "\n");
                    displayLoadedConfiguration();
                    return;
                } else {
                    System.out.println("Configuration file is missing required data or contains invalid values.\n");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while loading configuration: " + e.getMessage() + "\n");
            } catch (JsonSyntaxException e) {
                System.out.println("Invalid JSON syntax in the configuration file: " + e.getMessage() + "\n");
            } catch (NumberFormatException e) {
                System.out.println("An error occurred while parsing a number in the configuration file: " + e.getMessage() + "\n");
            }
        }

    }

    /**
     * Inner class representing the configuration data structure for Gson serialization/deserialization.
     */
    private static class ConfigurationData {
        private int maxTicketCapacity = Configuration.maxTicketCapacity;
        private int totalTickets = Configuration.totalTickets;
        private int ticketsPerRelease = Configuration.ticketsPerRelease;
        private int releaseIntervalMilliseconds = Configuration.releaseIntervalMilliseconds;
        private int ticketsPerRetrieval = Configuration.ticketsPerRetrieval;
        private int retrievalIntervalMilliseconds = Configuration.retrievalIntervalMilliseconds;
    }

    /**
     * Validates the configuration data to ensure all values are positive and constraints are satisfied.
     *
     * @param data the configuration data to validate.
     * @return true if the data is valid, false otherwise.
     */
    private static boolean isConfigurationDataValid(ConfigurationData data) {
        return data.maxTicketCapacity > 0 &&
                data.totalTickets > 0 &&
                data.ticketsPerRelease > 0 &&
                data.releaseIntervalMilliseconds > 0 &&
                data.ticketsPerRetrieval > 0 &&
                data.retrievalIntervalMilliseconds > 0 &&
                data.totalTickets < data.maxTicketCapacity &&
                data.ticketsPerRelease <= (data.maxTicketCapacity - data.totalTickets) &&
                data.ticketsPerRetrieval <= data.maxTicketCapacity;
    }

    /**
     * Displays the loaded configuration to the user.
     */
    private static void displayLoadedConfiguration() {
        System.out.println("=== Loaded Configuration ===");
        System.out.println("Max Ticket Capacity: " + maxTicketCapacity);
        System.out.println("Total Tickets: " + totalTickets);
        System.out.println("Tickets Per Release: " + ticketsPerRelease);
        System.out.println("Release Interval (ms): " + releaseIntervalMilliseconds);
        System.out.println("Tickets Per Retrieval: " + ticketsPerRetrieval);
        System.out.println("Retrieval Interval (ms): " + retrievalIntervalMilliseconds);
        System.out.println("============================\n");
    }

    /**
     * Retrieves the maximum ticket capacity of the system.
     *
     * @return the maximum ticket capacity.
     */
    public static int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    /**
     * Retrieves the total number of tickets initially available in the ticket pool.
     *
     * @return the total number of tickets.
     */
    public static int getTotalTickets() {
        return totalTickets;
    }

    /**
     * Retrieves the number of tickets released by vendors at a time.
     *
     * @return the number of tickets per release.
     */
    public static int getTicketsPerRelease() {
        return ticketsPerRelease;
    }

    /**
     * Retrieves the time interval (in milliseconds) between ticket releases.
     *
     * @return the release interval in milliseconds.
     */
    public static int getReleaseIntervalMilliseconds() {
        return releaseIntervalMilliseconds;
    }

    /**
     * Retrieves the number of tickets retrieved by customers at a time.
     *
     * @return the number of tickets per retrieval.
     */
    public static int getTicketsPerRetrieval() {
        return ticketsPerRetrieval;
    }

    /**
     * Retrieves the time interval (in milliseconds) between ticket retrievals.
     *
     * @return the retrieval interval in milliseconds.
     */
    public static int getRetrievalIntervalMilliseconds() {
        return retrievalIntervalMilliseconds;
    }
}
