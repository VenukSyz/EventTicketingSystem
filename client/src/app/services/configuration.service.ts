import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IApiResponseModel } from '../model/interface/api';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {

  constructor(private http: HttpClient) {}

  getAllConfigurations() : Observable<IApiResponseModel> {
    return this.http.get<IApiResponseModel>("http://localhost:8080/api/configuration/getAllConfigurations");
  }
}
