public class TicketPool {
    private int availableTickets;
    private final int maxCapacity;
    private int ticketsAddedByVendors;
    private final int maxTicketsToAdd;
    private int soldOutTickets;

    public TicketPool() {
        super();
        this.availableTickets = Configuration.getTotalTickets();
        this.maxCapacity = Configuration.getMaxTicketCapacity();
        this.ticketsAddedByVendors = 0;
        this.maxTicketsToAdd = maxCapacity - availableTickets;
        this.soldOutTickets = 0;
    }

    public synchronized boolean addTickets(int tickets, String name) {
        if (ticketsAddedByVendors == maxTicketsToAdd) {
            System.out.println("Cannot add " + tickets + " tickets by " + name + ". Vendor addition limit reached.");
            return false;
        }

        if (ticketsAddedByVendors + tickets > maxTicketsToAdd) {
            String msg = "Cannot add " + tickets + " tickets by " + name;
            tickets = maxTicketsToAdd - ticketsAddedByVendors;
            System.out.println(msg + " but added " + tickets + " tickets.");
        }

        availableTickets += tickets;
        ticketsAddedByVendors += tickets;
        System.out.println(name + " added " + tickets + " tickets. Total tickets now: " + availableTickets + " (Tickets added by vendors: " + ticketsAddedByVendors + ")");
        notifyAll();
        return true;
    }

    public synchronized boolean removeTickets(int tickets, String name) {
        if (soldOutTickets == maxCapacity) {
            System.out.println("All tickets sold out");
            return false;
        }

        if (soldOutTickets + tickets > maxCapacity) {
            System.out.println(name + " cannot buy " + tickets + " ticket/(s). Only " + (maxCapacity - soldOutTickets) + " ticket/(s) are available.");
        }

        while(availableTickets < tickets) {
            System.out.println("Not enough tickets available. "+ name + " is waiting...");
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(name + " interrupted while waiting.");
                return false;
            }
        }

        availableTickets -= tickets;
        soldOutTickets += tickets;
        System.out.println(name + " bought " + tickets + " tickets. Tickets left: " + availableTickets);
        notifyAll();
        return true;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public int getSoldOutTickets() {
        return soldOutTickets;
    }
}
