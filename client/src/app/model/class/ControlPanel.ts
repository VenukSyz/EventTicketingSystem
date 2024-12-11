/**
 * Represents a control panel configuration for an event, including event details
 * and ticketing information such as ticket price and number of vendors and customers.
 */
export class ControlPanel {
    /** Unique identifier for the control panel. */
    configId: number;

    /** Name of the event associated with the control panel. */
    eventName: string;

    /** Price of a single ticket for the event. */
    ticketPrice: number;

    /** Number of vendors participating in the event. */
    noOfVendors: number;

    /** Number of customers participating in the event. */
    noOfCustomers: number;

    /**
     * Initializes a new ControlPanel instance with default values.
     */
    constructor() {
        this.configId = 0;
        this.eventName = '';
        this.ticketPrice = 0;
        this.noOfVendors = 0;
        this.noOfCustomers = 0;
    }
}