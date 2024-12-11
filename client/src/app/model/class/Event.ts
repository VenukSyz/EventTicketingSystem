/**
 * Represents an event, including details such as the event name, ticket price,
 * maximum capacity, and the number of tickets that have been sold out.
 */
export class Event{
    /** Unique identifier for the event. */
    id: number;

    /** Name of the event. */
    eventName: string;

    /** Price of a single ticket for the event. */
    ticketPrice: number;

    /** Maximum capacity of tickets available for the event. */
    maxCapacity: number;

    /** Number of tickets that have been sold out for the event. */
    soldOutTickets: number;

    /**
     * Initializes a new Event instance with default values.
     */
    constructor() {
        this.id = 0;
        this.eventName = '';
        this.ticketPrice = 0;
        this.maxCapacity = 0;
        this.soldOutTickets = 0;
    }
}