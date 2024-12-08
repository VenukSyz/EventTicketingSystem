/**
 * Represents a Vendor in the ticketing system.
 * A Vendor periodically releases tickets into the shared {@link TicketPool}.
 * Extends the {@link User} class and implements specific functionality in the {@link Runnable#run()} method.
 */
public class Vendor extends User{
    /** Counter to generate unique vendor IDs. */
    private static long vendorCounter;
    /** The number of tickets released by the vendor at a time. */
    private final int ticketsPerRelease;
    /** The interval (in milliseconds) between successive ticket releases. */
    private final int releaseIntervalMilliseconds;


    /**
     * Constructs a new Vendor with the specified details and initializes its release parameters
     * using the {@link Configuration} class.
     *
     * @param ticketPool the shared {@link TicketPool} the vendor interacts with.
     * @param name the name of the vendor.
     * @param email the email address of the vendor.
     * @param phoneNum the phone number of the vendor.
     */
    public Vendor(TicketPool ticketPool,String name, String email, String phoneNum) {
        super(++vendorCounter, ticketPool, name, email, phoneNum);
        ticketsPerRelease = Configuration.getTicketsPerRelease();
        releaseIntervalMilliseconds = Configuration.getReleaseIntervalMilliseconds();
    }

    /**
     * Periodically releases tickets into the {@link TicketPool}.
     * The process continues until the pool is full or the vendor is interrupted.
     */
    @Override
    public void run() {
        try {
            while (true) {
                // Attempt to add tickets to the pool
                boolean ticketsAdded = this.getTicketPool().addTickets(ticketsPerRelease, this.getName());

                // If tickets cannot be added, stop releasing and exit the loop
                if (!ticketsAdded) {
                    synchronized (System.out) {
                        System.out.println(this.getName() + " is stopping as all tickets have been added.");
                    }
                    break;
                }

                // Wait for the specified interval before releasing the next batch of tickets
                Thread.sleep(releaseIntervalMilliseconds);
            }
        } catch (InterruptedException e) {
            // Handle thread interruption gracefully
            Thread.currentThread().interrupt();
            synchronized (System.out) {
                System.out.println(this.getName() + " interrupted");
            }
        }
    }
}
