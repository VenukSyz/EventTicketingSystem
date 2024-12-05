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
  ticketStatus: any = null;
  @Output() ticketsChanged = new EventEmitter<number>();

  constructor(private ticketStatusService: TicketStatusService) {}

  ngOnInit(): void {
    const btnFlag = sessionStorage.getItem('btnFlag');

    this.ticketStatusService.ticketStatus$.subscribe((status) => {
      this.ticketStatus = status;
      if (this.ticketStatus && this.ticketStatus.ticketsAddedByVendors !== undefined) {
        this.emitTicketsToParent(this.ticketStatus.ticketsAddedByVendors);
      }
    });

    if (btnFlag !== null) {
      const flag: number = parseInt(btnFlag, 10);
      if (flag === 0) {
        this.resetTicketStatus();
      }
    }
  }

  resetTicketStatus(): void {
    this.ticketStatus = null;
    sessionStorage.removeItem('status');
  }

  emitTicketsToParent(tickets: number): void {
    this.ticketsChanged.emit(tickets);
  }
}
