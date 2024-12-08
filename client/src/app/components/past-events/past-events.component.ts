import { Component, inject, OnInit, signal } from '@angular/core';
import { Event } from '../../model/class/Event';
import { EventService } from '../../services/event.service';
import { IApiResponseModel } from '../../model/interface/api';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-past-events',
  standalone: true,
  imports: [],
  templateUrl: './past-events.component.html',
  styleUrl: './past-events.component.css'
})
export class PastEventsComponent implements OnInit{
  eventService: EventService = inject(EventService);
  eventList = signal<Event[]>([]);
  isLoader: boolean = true;

  constructor (private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents() : void {
    this.eventService.getAllevents().subscribe((result: IApiResponseModel) => {
      this.eventList.set(result.data);
      this.isLoader = false;
    })
  }

  onDelete(id: number) {
    this.eventService.deleteEvent(id).subscribe((result: IApiResponseModel) => {
      if (result.data) {
        this.showSnackbar("Event deleted successfully!");
      } else {
        this.showSnackbar("Error deleting event!");
      }

      this.loadEvents();
    })
  }

  showSnackbar(message: string): void {
    this.snackBar.open(message, 'OK', {
      duration: 5000, // Auto dismiss after 3 seconds
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }
}
