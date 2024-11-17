public class Customer extends User{
    private static long customerCounter = 0;
    private final int ticketsPerRetrieval;
    private final int retrievalIntervalMilliseconds;

    public Customer(TicketPool ticketpool, String name, String email, String phoneNum) {
        super(++customerCounter, ticketpool, name, email, phoneNum);
        this.ticketsPerRetrieval = Configuration.getTicketsPerRetrieval();
        this.retrievalIntervalMilliseconds = Configuration.getRetrievalIntervalMilliseconds();
    }

    @Override
    public void run() {
        try {
            while (true) {
                boolean success = this.getTicketPool().removeTickets(ticketsPerRetrieval, this.getName());

                if (!success) {
                    System.out.println(this.getName() + " could not retrieve tickets");
                }

                Thread.sleep(retrievalIntervalMilliseconds);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(this.getName() + " interrupted.");
        }
    }
}
