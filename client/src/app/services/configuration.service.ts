import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IApiResponseModel } from '../model/interface/api';
import { Configuration } from '../model/class/Configuration';
import { environment } from '../../environments/environment.development';
import { Constant } from '../constant/Constant';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {

  /**
   * Creates an instance of ConfigurationService.
   * @param http - The HttpClient instance used to make HTTP requests.
   */
  constructor(private http: HttpClient) {}

  /**
   * Fetches all configurations from the backend.
   * @returns An observable of IApiResponseModel containing the fetched configurations.
   */
  getAllConfigurations() : Observable<IApiResponseModel> {
    return this.http.get<IApiResponseModel>(environment.API_URL + Constant.CONFIG_API_METHODS.GET_ALL_CONFIGS);
  }

  /**
   * Saves or updates a configuration object in the backend.
   * @param configurationObj - The Configuration object containing the data to save or update.
   * @returns An observable of IApiResponseModel indicating the success or failure of the operation.
   */
  saveUpdateConfiguration(configurationObj: Configuration) : Observable<IApiResponseModel> {
    return this.http.post<IApiResponseModel>(environment.API_URL + Constant.CONFIG_API_METHODS.SAVE_UPDATE_CONFIG, configurationObj);
  }

  /**
   * Deletes a configuration by its ID.
   * @param configurationId - The ID of the configuration to delete.
   * @returns An observable of IApiResponseModel indicating the success or failure of the deletion.
   */
  deleteConfiguration(configurationId: number) : Observable<IApiResponseModel> {
    return this.http.delete<IApiResponseModel>(environment.API_URL + Constant.CONFIG_API_METHODS.DELETE_CONFIG + configurationId);
  }
}
