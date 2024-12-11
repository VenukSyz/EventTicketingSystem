import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IApiResponseModel } from '../model/interface/api';
import { environment } from '../../environments/environment.development';
import { Constant } from '../constant/Constant';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  /**
   * Creates an instance of EventService.
   * @param http - The HttpClient instance used for making HTTP requests.
   */
  constructor(private http: HttpClient) { }

  /**
   * Fetches all events from the server.
   * This method sends a GET request to the backend to retrieve a list of all events.
   * 
   * @returns An Observable that emits the response from the server, which includes a list of events.
   */
  getAllevents() : Observable<IApiResponseModel> {
    return this.http.get<IApiResponseModel>(environment.API_URL + Constant.EVENT_API_METHODS.GET_ALL_EVENTS);
  }

  /**
   * Deletes an event based on the provided event ID.
   * This method sends a DELETE request to the server to remove an event from the system.
   * 
   * @param id - The ID of the event to be deleted.
   * @returns An Observable that emits the server's response indicating whether the event was successfully deleted.
   */
  deleteEvent(id: number): Observable<IApiResponseModel> {
    return this.http.delete<IApiResponseModel>(environment.API_URL + Constant.EVENT_API_METHODS.DELETE_EVENT + id);
  }
}
