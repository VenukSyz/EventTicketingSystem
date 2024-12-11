import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TicketStatusService {

  /** 
   * A BehaviorSubject to hold the ticket system status. 
   * Initialized with a value from sessionStorage or null if no status is saved. 
   */
  private ticketStatusSubject: BehaviorSubject<any>;

  /** 
   * An Observable exposing the current ticket system status for subscription. 
   */
  ticketStatus$;

  /**
   * Creates an instance of the TicketStatusService.
   * Initializes the ticketStatusSubject with a value from sessionStorage or defaults to null.
   */
  constructor() {
    // Initialize from sessionStorage or default to null
    const savedStatus = sessionStorage.getItem('status');
    const initialValue = savedStatus ? JSON.parse(savedStatus) : null;

    this.ticketStatusSubject = new BehaviorSubject<any>(initialValue);
    this.ticketStatus$ = this.ticketStatusSubject.asObservable();
  }

  /**
   * Updates the system status with a new value and stores it in sessionStorage.
   * 
   * @param status - The new status to be set for the ticket system.
   */
  updateSystemStatus(status: any): void {
    this.ticketStatusSubject.next(status);
    sessionStorage.setItem('status', JSON.stringify(status));
  }

  /**
   * Gets the current system status from the BehaviorSubject.
   * 
   * @returns The current status value.
   */
  getSystemStatus(): any {
    return this.ticketStatusSubject.value;
  }
}
