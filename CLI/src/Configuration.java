import com.google.gson.Gson;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Configuration {
    private static int maxTicketCapacity;
    private static int totalTickets;
    private static int ticketsPerRelease;
    private static int releaseIntervalMilliseconds;
    private static int ticketsPerRetrieval;
    private static int retrievalIntervalMilliseconds;

    private static final Gson gson = new Gson();
    private static final Scanner input = new Scanner(System.in);

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

    public static void initialize() {
        System.out.println("=== Initializing System Configuration ===");
        maxTicketCapacity = validateInput("Enter the max ticket capacity: ");
        totalTickets = validateWithLimit("Enter total number of tickets available in the pool: ", maxTicketCapacity);
        ticketsPerRelease = validateWithLimit("Enter the number of tickets each vendor will release at a time: ", maxTicketCapacity - totalTickets);
        releaseIntervalMilliseconds = validateInput("Enter the release interval in milliseconds: ");
        ticketsPerRetrieval = validateWithLimit("Enter the number of tickets each customer will attempt to retrieve at a time: ", maxTicketCapacity);
        retrievalIntervalMilliseconds = validateInput("Enter the retrieval interval in milliseconds: ");
    }

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

    public static void saveToFile() {
        String fileName = getFileName();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            gson.toJson(new ConfigurationData(), writer);
            System.out.println("Configuration saved to " + fileName + "\n");
        } catch (IOException e) {
            System.out.println("An error occurred while saving configuration: " + e.getMessage() + "\n");
        }
    }

    public static void loadFromFile() {
        String fileName;
        while (true) {
            fileName = getFileName();
            File file = new File(fileName);

            // Check if the file exists
            if (!file.exists()) {
                System.out.println("File " + fileName + " does not exist.\n");
                System.out.print("Enter '1' to re-enter the file name, or '2' to manually configure settings: ");
                String choice = input.nextLine().trim();

                if (choice.equals("2")) {
                    initialize();  // Prompt user to manually configure settings
                    return;
                } else if (choice.equals("1")) {
                    continue;
                } else {
                    System.out.println("Invalid option. Please enter '1' or '2'.");
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
            } else {
                System.out.println("Configuration file is missing required data or contains invalid values.\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading configuration: " + e.getMessage() + "\n");
        }

    }

    // Inner class to represent the configuration data structure for Gson
    private static class ConfigurationData {
        private int maxTicketCapacity = Configuration.maxTicketCapacity;
        private int totalTickets = Configuration.totalTickets;
        private int ticketsPerRelease = Configuration.ticketsPerRelease;
        private int releaseIntervalMilliseconds = Configuration.releaseIntervalMilliseconds;
        private int ticketsPerRetrieval = Configuration.ticketsPerRetrieval;
        private int retrievalIntervalMilliseconds = Configuration.retrievalIntervalMilliseconds;
    }

    // Helper method to validate configuration data
    private static boolean isConfigurationDataValid(ConfigurationData data) {
        return data.maxTicketCapacity > 0 &&
                data.totalTickets > 0 &&
                data.ticketsPerRelease > 0 &&
                data.releaseIntervalMilliseconds > 0 &&
                data.ticketsPerRetrieval > 0 &&
                data.retrievalIntervalMilliseconds > 0 &&
                data.totalTickets <= data.maxTicketCapacity &&
                data.ticketsPerRelease <= (data.maxTicketCapacity - data.totalTickets) &&
                data.ticketsPerRetrieval <= data.maxTicketCapacity;
    }

    public static int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public static int getTotalTickets() {
        return totalTickets;
    }

    public static int getTicketsPerRelease() {
        return ticketsPerRelease;
    }

    public static int getReleaseIntervalMilliseconds() {
        return releaseIntervalMilliseconds;
    }

    public static int getTicketsPerRetrieval() {
        return ticketsPerRetrieval;
    }

    public static int getRetrievalIntervalMilliseconds() {
        return retrievalIntervalMilliseconds;
    }
}
