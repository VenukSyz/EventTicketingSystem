/**
 * Represents a configuration for ticket management in the system.
 * Stores information related to ticket capacity, release, and retrieval settings.
 */
export class Configuration{
    /** Unique identifier for the configuration. */
    id: number;

    /** Name of the configuration. */
    name: string;

    /** Maximum capacity for tickets in the system. */
    maxTicketCapacity: number;

    /** Total number of tickets in the configuration. */
    totalTickets: number;

    /** Number of tickets to be released at each interval. */
    ticketsPerRelease: number;

    /** Time interval (in milliseconds) between each release of tickets. */
    releaseIntervalMilliseconds: number;

    /** Number of tickets to be retrieved at each interval. */
    ticketsPerRetrieval: number;

    /** Time interval (in milliseconds) between each retrieval of tickets. */
    retrievalIntervalMilliseconds: number;

    /**
     * Initializes a new Configuration instance with default values.
     */
    constructor() {
        this.id = 0;
        this.name = "";
        this.maxTicketCapacity = 0;
        this.totalTickets = 0;
        this.ticketsPerRelease = 0;
        this.releaseIntervalMilliseconds = 0;
        this.ticketsPerRetrieval = 0;
        this.retrievalIntervalMilliseconds = 0;
    }
}