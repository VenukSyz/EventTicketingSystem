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
    const btnFlag = sessionStorage.getItem('btnFlag');

    this.ticketStatusService.ticketStatus$.subscribe((status) => {
      this.ticketStatus = status;
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
}
