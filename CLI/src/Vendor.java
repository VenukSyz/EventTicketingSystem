public class Vendor extends User{
    private static long vendorCounter;
    private final int ticketsPerRelease;
    private final int releaseIntervalMilliseconds;


    public Vendor(TicketPool ticketPool,String name, String email, String phoneNum) {
        super(++vendorCounter, ticketPool, name, email, phoneNum);
        ticketsPerRelease = Configuration.getTicketsPerRelease();
        releaseIntervalMilliseconds = Configuration.getReleaseIntervalMilliseconds();
    }

    @Override
    public void run() {
        try {
            while (true) {
                boolean ticketsAdded = this.getTicketPool().addTickets(ticketsPerRelease, this.getName());
                if (!ticketsAdded) {
                    synchronized (System.out) {
                        System.out.println(this.getName() + " is stopping as all tickets have been added.");
                    }
                    break;
                }

                Thread.sleep(releaseIntervalMilliseconds);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            synchronized (System.out) {
                System.out.println(this.getName() + " interrupted");
            }
        }
    }
}
