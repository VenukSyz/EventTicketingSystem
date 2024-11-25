import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IApiResponseModel } from '../model/interface/api';
import { Configuration } from '../model/class/Configuration';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {

  constructor(private http: HttpClient) {}

  getAllConfigurations() : Observable<IApiResponseModel> {
    return this.http.get<IApiResponseModel>("http://localhost:8080/api/configuration/getAllConfigurations");
  }

  saveUpdateConfiguration(configurationObj: Configuration) : Observable<IApiResponseModel> {
    return this.http.post<IApiResponseModel>("http://localhost:8080/api/configuration/saveUpdateConfiguration",configurationObj);
  }

  deleteConfiguration(configurationId: number) : Observable<IApiResponseModel> {
    return this.http.delete<IApiResponseModel>(`http://localhost:8080/api/configuration/deleteConfiguration/${configurationId}`);
  }
}
