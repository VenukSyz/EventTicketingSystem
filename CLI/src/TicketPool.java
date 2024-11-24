import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketPool {
    private final List<Integer> availableTicketsInPool;
    private final List<Integer> ticketsAddedByVendors;
    private final List<Integer> soldOutTickets;
    private final int maxCapacity;
    private final int maxTicketsToAdd;
    private int toBeSoldOutTickets;
    private int totalTickets;

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

    public int getAvailableTicketsInPool() {
        return availableTicketsInPool.size();
    }

    public int getSoldOutTickets() {
        return soldOutTickets.size();
    }

    public int getToBeSoldOutTickets() {
        return toBeSoldOutTickets;
    }
}
