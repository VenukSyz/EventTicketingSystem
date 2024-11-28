package com.example.server.logic;

public abstract class UserLogic implements Runnable{
    private long id;
    private final TicketPoolLogic ticketPool;
    private String name;
    private String email;
    private String phoneNum;

    public UserLogic(long id, TicketPoolLogic ticketPool,String name, String email, String phoneNum) {
        this.id = id;
        this.ticketPool = ticketPool;
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    public TicketPoolLogic getTicketPool() {
        return ticketPool;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}

