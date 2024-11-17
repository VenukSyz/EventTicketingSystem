public class TicketPool {
    private int availableTicketsInPool;
    private final int maxCapacity;
    private int ticketsAddedByVendors;
    private final int maxTicketsToAdd;
    private int soldOutTickets;
    private int toBeSoldOutTickets;

    public TicketPool() {
        super();
        this.availableTicketsInPool = Configuration.getTotalTickets();
        this.maxCapacity = Configuration.getMaxTicketCapacity();
        this.ticketsAddedByVendors = 0;
        this.maxTicketsToAdd = maxCapacity - availableTicketsInPool;
        this.soldOutTickets = 0;
        this.toBeSoldOutTickets = Configuration.getMaxTicketCapacity();
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

        availableTicketsInPool += tickets;
        ticketsAddedByVendors += tickets;
        System.out.println(name + " added " + tickets + " tickets. Total tickets now: " + availableTicketsInPool + " (Tickets added by vendors: " + ticketsAddedByVendors + ")");
        notifyAll();
        return true;
    }

    public synchronized boolean removeTickets(int tickets, String name) {
        while(availableTicketsInPool < tickets) {
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

        availableTicketsInPool -= tickets;
        toBeSoldOutTickets -= tickets;
        soldOutTickets += tickets;
        System.out.println(name + " bought " + tickets + " tickets. Tickets left: " + availableTicketsInPool);
        notifyAll();
        return true;
    }

    public int getAvailableTicketsInPool() {
        return availableTicketsInPool;
    }

    public int getSoldOutTickets() {
        return soldOutTickets;
    }

    public int getToBeSoldOutTickets() {
        return toBeSoldOutTickets;
    }
}
