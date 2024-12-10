/**
 * Represents a Customer in the ticketing system.
 * A Customer periodically attempts to retrieve tickets from the shared {@link TicketPool}.
 * Extends the {@link User} class and implements specific functionality in the {@link Runnable#run()} method.
 */
public class Customer extends User{
    /** Counter to generate unique vendor IDs. */
    private static long customerCounter = 0;
    /** The number of tickets the customer attempts to retrieve at a time. */
    private final int ticketsPerRetrieval;
    /** The interval (in milliseconds) between successive ticket retrieval attempts. */
    private final int retrievalIntervalMilliseconds;

    /**
     * Constructs a new Customer with the specified details and initializes its retrieval parameters
     * using the {@link Configuration} class.
     *
     * @param ticketPool the shared {@link TicketPool} the customer interacts with.
     * @param name the name of the customer.
     * @param email the email address of the customer.
     * @param phoneNum the phone number of the customer.
     */
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
                boolean ticketsBought = this.getTicketPool().removeTickets(ticketsPerRetrieval, this.getName());

                if (!ticketsBought) {
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
