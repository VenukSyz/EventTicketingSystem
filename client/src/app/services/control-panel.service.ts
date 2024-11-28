import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ControlPanel } from '../model/class/ControlPanel';
import { Observable } from 'rxjs';
import { IApiResponseModel } from '../model/interface/api';

@Injectable({
  providedIn: 'root'
})
export class ControlPanelService {

  constructor(private http: HttpClient) {}
  
  startSystem(controlPanelObj: ControlPanel) : Observable<IApiResponseModel> {
    return this.http.post<IApiResponseModel>("http://localhost:8080/api/controlPanel/start",controlPanelObj);
  }

  resumeSystem() : Observable<IApiResponseModel> {
    return this.http.post<IApiResponseModel>("http://localhost:8080/api/controlPanel/resume",null);
  }

  stopSystem() : Observable<IApiResponseModel> {
    return this.http.post<IApiResponseModel>("http://localhost:8080/api/controlPanel/stop",null);
  }
}
