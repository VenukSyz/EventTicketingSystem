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
  /** Service for handling event-related operations. */
  eventService: EventService = inject(EventService);

  /** Reactive signal holding the list of past events. */
  eventList = signal<Event[]>([]);

  /** Flag indicating whether the loader/spinner is active. */
  isLoader: boolean = true;

   /**
   * Initializes the component with required services.
   * @param snackBar Service to display notifications to the user.
   */
  constructor (private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.loadEvents();
  }

  /**
   * Fetches all past events from the service and updates the event list.
   */
  loadEvents() : void {
    this.eventService.getAllevents().subscribe((result: IApiResponseModel) => {
      this.eventList.set(result.data);
      this.isLoader = false;
    })
  }

  /**
   * Deletes an event by its ID and reloads the event list.
   * Displays a notification indicating success or failure.
   * @param id The ID of the event to delete.
   */
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

  /**
   * Displays a snackbar notification with the provided message.
   * @param message The message to display in the snackbar.
   */
  showSnackbar(message: string): void {
    this.snackBar.open(message, 'OK', {
      duration: 5000, // Auto dismiss after 3 seconds
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }
}
