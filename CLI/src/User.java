/**
 * Abstract class representing a User in the ticketing system.
 * The User can interact with a {@link TicketPool} to retrieve or release tickets
 * and implements {@link Runnable} to support multithreading for concurrent operations.
 */
public abstract class User implements Runnable{
    /** Unique identifier for the user. */
    private long id;
    /** The shared {@link TicketPool} instance the user interacts with. */
    private final TicketPool ticketPool;
    /** The name of the user. */
    private String name;
    /** The email address of the user. */
    private String email;
    /** The phone number of the user. */
    private String phoneNum;

    /**
     * Constructs a new User with the specified details.
     *
     * @param id the unique identifier for the user.
     * @param ticketPool the shared {@link TicketPool} instance.
     * @param name the name of the user.
     * @param email the email address of the user.
     * @param phoneNum the phone number of the user.
     */
    public User(long id, TicketPool ticketPool,String name, String email, String phoneNum) {
        this.id = id;
        this.ticketPool = ticketPool;
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    /**
     * Gets the {@link TicketPool} associated with the user.
     *
     * @return the {@link TicketPool} instance.
     */
    public TicketPool getTicketPool() {
        return ticketPool;
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return the user's ID.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param id the new ID for the user.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the user.
     *
     * @return the user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the new name for the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the user's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the new email address for the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Gets the phone number of the user.
     *
     * @return the user's phone number.
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * Sets the phone number of the user.
     *
     * @param phoneNum the new phone number for the user.
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
