public abstract class User implements Runnable{
    private long id;
    private final TicketPool ticketPool;
    private String name;
    private String email;
    private String phoneNum;

    public User(long id, TicketPool ticketPool,String name, String email, String phoneNum) {
        this.id = id;
        this.ticketPool = ticketPool;
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    public TicketPool getTicketPool() {
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
