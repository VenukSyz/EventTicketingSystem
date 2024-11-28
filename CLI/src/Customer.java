public class Customer extends User{
    private static long customerCounter = 0;
    private final int ticketsPerRetrieval;
    private final int retrievalIntervalMilliseconds;

    public Customer(TicketPool ticketPool, String name, String email, String phoneNum) {
        super(++customerCounter, ticketPool, name, email, phoneNum);
        this.ticketsPerRetrieval = Configuration.getTicketsPerRetrieval();
        this.retrievalIntervalMilliseconds = Configuration.getRetrievalIntervalMilliseconds();
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (this.getTicketPool()) {
                    if (this.getTicketPool().getToBeSoldOutTickets() < this.ticketsPerRetrieval) {
                        synchronized (System.out) {
                            System.out.println(this.getName() + " stopping: Not enough tickets left to retrieve.");
                        }
                        break;
                    }
                }
                boolean success = this.getTicketPool().removeTickets(ticketsPerRetrieval, this.getName());

                if (!success) {
                    synchronized (System.out) {
                        System.out.println(this.getName() + " could not retrieve tickets");
                    }
                }

                Thread.sleep(retrievalIntervalMilliseconds);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            synchronized (System.out) {
                System.out.println(this.getName() + " interrupted.");
            }
        }
    }
}
