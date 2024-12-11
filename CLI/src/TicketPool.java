import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the pool of tickets in the ticketing system.
 * Allows vendors to add tickets and customers to retrieve tickets, ensuring synchronization
 * for concurrent operations in a multithreaded environment.
 */
public class TicketPool {
    /** List of tickets currently available in the pool. */
    private final List<Integer> availableTicketsInPool;
    /** List of tickets added by vendors to the pool. */
    private final List<Integer> ticketsAddedByVendors;
    /** List of tickets that have been sold out. */
    private final List<Integer> soldOutTickets;
    /** Maximum capacity of tickets the pool can hold. */
    private final int maxCapacity;
    /** Maximum number of tickets that can be added by vendors. */
    private final int maxTicketsToAdd;
    /** Number of tickets remaining to be sold. */
    private int toBeSoldOutTickets;
    /** Number of tickets that's being added to the pool when the system starts. */
    private int totalTickets;

    /**
     * Initializes the ticket pool with the specified configuration values.
     * Preloads the pool with tickets based on the initial total ticket count.
     */
    public TicketPool() {
        super();
        this.availableTicketsInPool = Collections.synchronizedList(new ArrayList<>());
        this.ticketsAddedByVendors = Collections.synchronizedList(new ArrayList<>());
        this.soldOutTickets = Collections.synchronizedList(new ArrayList<>());
        this.maxCapacity = Configuration.getMaxTicketCapacity();
        this.maxTicketsToAdd = maxCapacity - Configuration.getTotalTickets();
        this.toBeSoldOutTickets = Configuration.getMaxTicketCapacity();
        this.totalTickets = Configuration.getTotalTickets();

        for (int i = 0; i < totalTickets; i++) {
            this.availableTicketsInPool.add(i + 1);
        }
    }

    /**
     * Allows a vendor to add tickets to the pool.
     * Ensures the addition respects the maximum capacity and tickets addition limit.
     *
     * @param tickets the number of tickets to be added.
     * @param name the name of the vendor attempting to add tickets.
     * @return true if tickets were successfully added, false otherwise.
     */
    public synchronized boolean addTickets(int tickets, String name) {
        if (ticketsAddedByVendors.size() == maxTicketsToAdd) {
            System.out.println("Cannot add " + tickets + " tickets by " + name + ". Vendor addition limit reached.");
            return false;
        }

        if (ticketsAddedByVendors.size() + tickets > maxTicketsToAdd) {
            String msg = "Cannot add " + tickets + " tickets by " + name;
            tickets = maxTicketsToAdd - ticketsAddedByVendors.size();
            System.out.println(msg + " but added " + tickets + " tickets.");
        }

        for (int i = 0; i < tickets; i++) {
            availableTicketsInPool.add(++totalTickets);
            ticketsAddedByVendors.add(ticketsAddedByVendors.size() + 1);
        }

        System.out.println(name + " added " + tickets + " tickets. Total tickets now: " + availableTicketsInPool.size() + " (Tickets added by vendors: " + ticketsAddedByVendors.size() + ")");
        notifyAll();
        return true;
    }

    /**
     * Allows a customer to remove tickets from the pool.
     * Waits if there are not enough tickets available and notifies threads upon completion.
     *
     * @param tickets the number of tickets to be removed.
     * @param name the name of the customer attempting to retrieve tickets.
     * @return true if tickets were successfully retrieved, false otherwise.
     */
    public synchronized boolean removeTickets(int tickets, String name) {
        while(availableTicketsInPool.size() < tickets) {
            System.out.println("Not enough tickets available. "+ name + " is waiting...");
            try {
                wait();
                if (toBeSoldOutTickets < tickets) {
                    System.out.println("Not enough tickets remaining for any customers. Current tickets left: " + toBeSoldOutTickets);
                    return false;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(name + " interrupted while waiting.");
                return false;
            }
        }

        for (int i = 0; i < tickets; i++) {
            soldOutTickets.add(availableTicketsInPool.get(0));
            availableTicketsInPool.remove(0);
        }

        toBeSoldOutTickets -= tickets;
        System.out.println(name + " bought " + tickets + " tickets. Tickets left: " + availableTicketsInPool.size());
        notifyAll();
        return true;
    }

    /**
     * Retrieves the number of tickets currently available in the pool.
     *
     * @return the size of the available tickets list.
     */
    public int getAvailableTicketsInPool() {
        return availableTicketsInPool.size();
    }

    /**
     * Retrieves the number of tickets that have been sold out.
     *
     * @return the size of the sold-out tickets list.
     */
    public int getSoldOutTickets() {
        return soldOutTickets.size();
    }

    /**
     * Retrieves the number of tickets remaining to be sold.
     *
     * @return the number of tickets left to be sold.
     */
    public int getToBeSoldOutTickets() {
        return toBeSoldOutTickets;
    }
}
