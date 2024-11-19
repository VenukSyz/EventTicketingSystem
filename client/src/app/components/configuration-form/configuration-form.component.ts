import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-configuration-form',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './configuration-form.component.html',
  styleUrl: './configuration-form.component.css'
})
export class ConfigurationFormComponent {
  maxTicketCapacity: number = 0;
  totalTickets: number = 0;
  ticketsPerRelease: number = 0;
  releaseIntervalMilliseconds: number = 0;
  ticketsPerRetrieval: number = 0;
  retrievalIntervalMilliseconds: number = 0;

  isTotalTicketsValid() : boolean {
    return this.totalTickets <= this.maxTicketCapacity;
  }

  isTicketPerReleaseValid() : boolean {
    return this.ticketsPerRelease <= this.maxTicketCapacity - this.totalTickets;
  }

  isTicketsPerRetrievalValid() : boolean {
    return this.ticketsPerRetrieval <= this.maxTicketCapacity
  }

}
