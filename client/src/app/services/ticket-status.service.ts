import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TicketStatusService {

  private ticketStatusSubject: BehaviorSubject<any>;
  ticketStatus$; // Expose as observable

  constructor() {
    // Initialize from sessionStorage or default to null
    const savedStatus = sessionStorage.getItem('status');
    const initialValue = savedStatus ? JSON.parse(savedStatus) : null;

    this.ticketStatusSubject = new BehaviorSubject<any>(initialValue);
    this.ticketStatus$ = this.ticketStatusSubject.asObservable();
  }

  updateSystemStatus(status: any): void {
    this.ticketStatusSubject.next(status);
    sessionStorage.setItem('status', JSON.stringify(status));
  }

  getSystemStatus(): any {
    return this.ticketStatusSubject.value;
  }
}
