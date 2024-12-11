import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { TicketStatusService } from '../../services/ticket-status.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-ticket-status',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ticket-status.component.html',
  styleUrl: './ticket-status.component.css'
})
export class TicketStatusComponent implements OnInit{
  /** Stores the current ticket status received from the service. */
  ticketStatus: any = null;

  /** Emits an event when ticket data changes, passing the number of tickets and sold-out tickets. */
  @Output() ticketsChanged = new EventEmitter<{ tickets: number, soldOutTickets: number }>();

  /**
   * Initializes the TicketStatusComponent.
   * @param ticketStatusService Service to manage ticket status updates.
   */
  constructor(private ticketStatusService: TicketStatusService) {}

  ngOnInit(): void {
    let btnFlag = sessionStorage.getItem('btnFlag');
    
    this.ticketStatusService.ticketStatus$.subscribe((status) => {
      this.ticketStatus = status;
      if (this.ticketStatus && this.ticketStatus.ticketsAddedByVendors !== undefined) {
        btnFlag = sessionStorage.getItem('btnFlag');
        if (btnFlag !== null) {
          const flag: number = parseInt(btnFlag, 10);
          if (flag != 0) {
            this.emitTicketsToParent(this.ticketStatus.ticketsAddedByVendors,this.ticketStatus.soldOutTickets);
          }
        }
      }
    });

    if (btnFlag !== null) {
      const flag: number = parseInt(btnFlag, 10);
      if (flag === 0) {
        this.resetTicketStatus();
      }
    }
  }

  /**
   * Resets the ticket status to its initial state and clears session data.
   */
  resetTicketStatus(): void {
    this.ticketStatus = null;
    sessionStorage.removeItem('status');
  }

  /**
   * Emits ticket data to the parent component.
   * @param tickets The number of tickets added by vendors.
   * @param soldOutTickets The number of tickets that are sold out.
   */
  emitTicketsToParent(tickets: number, soldOutTickets: number): void {
    this.ticketsChanged.emit({tickets, soldOutTickets});
  }
}
