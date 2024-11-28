import { Component, OnInit } from '@angular/core';
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

  constructor(private ticketStatusService: TicketStatusService) {}

  ngOnInit(): void {
    // Subscribe to systemStatus updates
    this.ticketStatusService.ticketStatus$.subscribe((status) => {
      this.ticketStatus = status;
    });
  }

  resetTicketStatus(): void {
    this.ticketStatus = null;
  }
}
