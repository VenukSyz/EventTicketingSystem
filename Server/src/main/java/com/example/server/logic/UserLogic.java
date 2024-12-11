package com.example.server.logic;

import com.example.server.service.LogBroadcaster;

/**
 * Abstract base class representing a user in the system.
 * Provides common properties and behavior for users such as Vendors and Customers.
 * Implements {@link Runnable} to allow for multi-threaded operation.
 */
public abstract class UserLogic implements Runnable{
    private long id;
    private final TicketPoolLogic ticketPool;
    private final LogBroadcaster logBroadcaster;
    private String name;
    private String email;
    private String phoneNum;

    /**
     * Constructs a UserLogic instance with the specified details.
     *
     * @param id            the unique identifier of the user
     * @param logBroadcaster the log broadcaster for logging activities
     * @param ticketPool    the ticket pool associated with the user
     * @param name          the name of the user
     * @param email         the email address of the user
     * @param phoneNum      the phone number of the user
     */
    public UserLogic(long id, LogBroadcaster logBroadcaster, TicketPoolLogic ticketPool,String name, String email, String phoneNum) {
        this.id = id;
        this.logBroadcaster = logBroadcaster;
        this.ticketPool = ticketPool;
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    /**
     * Gets the ticket pool associated with this user.
     *
     * @return the {@link TicketPoolLogic} instance
     */
    public TicketPoolLogic getTicketPool() {
        return ticketPool;
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return the user ID
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id the new user ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the user.
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the new name for the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the new email address for the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone number of the user.
     *
     * @return the user's phone number
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * Sets the phone number of the user.
     *
     * @param phoneNum the new phone number for the user
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    /**
     * Gets the log broadcaster for this user.
     *
     * @return the {@link LogBroadcaster} instance
     */
    public LogBroadcaster getLogBroadcaster() {
        return logBroadcaster;
    }
}

