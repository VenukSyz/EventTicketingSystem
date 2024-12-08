import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IApiResponseModel } from '../model/interface/api';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  constructor(private http: HttpClient) { }

  getAllevents() : Observable<IApiResponseModel> {
    return this.http.get<IApiResponseModel>("http://localhost:8080/api/event/getAllEvents");
  }

  deleteEvent(id: number): Observable<IApiResponseModel> {
    return this.http.delete<IApiResponseModel>(`http://localhost:8080/api/event/deleteEvent/${id}`);
  }
}
