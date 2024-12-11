import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ControlPanel } from '../model/class/ControlPanel';
import { Observable } from 'rxjs';
import { IApiResponseModel } from '../model/interface/api';
import { environment } from '../../environments/environment.development';
import { Constant } from '../constant/Constant';

@Injectable({
  providedIn: 'root'
})
export class ControlPanelService {

  /**
   * Creates an instance of ControlPanelService.
   * @param http - The HttpClient instance used for making HTTP requests.
   */
  constructor(private http: HttpClient) {}
  
  /**
   * Starts the system using the provided control panel object.
   * Sends a POST request to start the system.
   * @param controlPanelObj - The ControlPanel object containing details to start the system.
   * @returns An observable of IApiResponseModel indicating the success or failure of the operation.
   */
  startSystem(controlPanelObj: ControlPanel) : Observable<IApiResponseModel> {
    return this.http.post<IApiResponseModel>(environment.API_URL + Constant.CONTROL_PANEL_API_METHODS.START, controlPanelObj);
  }

  /**
   * Resumes the system.
   * Sends a POST request to resume the system.
   * @returns An observable of IApiResponseModel indicating the success or failure of the operation.
   */
  resumeSystem(controlPanelObj: ControlPanel) : Observable<IApiResponseModel> {
    return this.http.post<IApiResponseModel>(environment.API_URL + Constant.CONTROL_PANEL_API_METHODS.RESUME, controlPanelObj);
  }

  /**
   * Stops the system.
   * Sends a POST request to stop the system.
   * @returns An observable of IApiResponseModel indicating the success or failure of the operation.
   */
  stopSystem() : Observable<IApiResponseModel> {
    return this.http.post<IApiResponseModel>(environment.API_URL + Constant.CONTROL_PANEL_API_METHODS.STOP, null);
  }
}
