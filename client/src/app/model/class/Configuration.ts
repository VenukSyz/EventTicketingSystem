export class Configuration{
    id: number;
    name: string;
    maxTicketCapacity: number;
    totalTickets: number;
    ticketsPerRelease: number;
    releaseIntervalMilliseconds: number;
    ticketsPerRetrieval: number;
    retrievalIntervalMilliseconds: number;

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