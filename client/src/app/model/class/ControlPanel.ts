export class ControlPanel {
    id: number;
    eventName: string;
    ticketPrice: number;
    noOfVendors: number;
    noOfCustomers: number;

    constructor() {
        this.id = 0;
        this.eventName = '';
        this.ticketPrice = 0;
        this.noOfVendors = 0;
        this.noOfCustomers = 0;
    }
}