import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TicketStatusService {

  private ticketStatusSubject = new BehaviorSubject<any>(null);
  ticketStatus$ = this.ticketStatusSubject.asObservable();

  updateSystemStatus(status: any): void {
    this.ticketStatusSubject.next(status);
  }

  getSystemStatus(): any {
    return this.ticketStatusSubject.value;
  }
}
