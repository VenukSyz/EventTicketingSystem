export class Event{
    id: number;
    eventName: string;
    ticketPrice: number;
    maxCapacity: number;
    soldOutTickets: number;

    constructor() {
        this.id = 0;
        this.eventName = '';
        this.ticketPrice = 0;
        this.maxCapacity = 0;
        this.soldOutTickets = 0;
    }
}