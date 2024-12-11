import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ConfigurationService } from '../../services/configuration.service';
import { Configuration } from '../../model/class/Configuration';
import { IApiResponseModel } from '../../model/interface/api';
import { MatSnackBar } from '@angular/material/snack-bar';


/**
 * ConfigurationFormComponent
 * Manages the configuration form, including CRUD operations for configurations.
 */
@Component({
  selector: 'app-configuration-form',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './configuration-form.component.html',
  styleUrl: './configuration-form.component.css'
})

export class ConfigurationFormComponent implements OnInit {

  configurationService: ConfigurationService = inject(ConfigurationService);
  configurationObj: Configuration = new Configuration();

  /** Reactive signal holding the list of configurations. */
  configurationList = signal<Configuration[]>([]);

  /** Flag indicating whether the loader is active. */
  isLoader: boolean = true;

  /** Button text for save/update actions. */
  btnText: string = 'Save';

  /**
   * Initializes the component with required services.
   * @param snackBar Service to display notifications to the user.
   */
  constructor(private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.loadConfigurations();
  }

  /**
   * Fetches all configurations from the service and updates the list.
   */
  loadConfigurations(): void {
    this.configurationService.getAllConfigurations().subscribe((result: IApiResponseModel) => {
      this.configurationList.set(result.data);
      this.isLoader = false;
    });
  }

  /**
   * Checks if the total number of tickets is valid.
   * @returns true if total tickets do not exceed max capacity.
   */
  get isTotalTicketsValid(): boolean {
    return this.configurationObj.totalTickets <= this.configurationObj.maxTicketCapacity;
  }

  /**
   * Validates tickets per release against the maximum ticket capacity and current total tickets.
   * @returns true if tickets per release are within valid limits.
   */
  get isTicketPerReleaseValid(): boolean {
    return this.configurationObj.ticketsPerRelease <= this.configurationObj.maxTicketCapacity - this.configurationObj.totalTickets;
  }

  /**
   * Validates tickets per retrieval against the maximum ticket capacity.
   * @returns true if tickets per retrieval are within valid limits.
   */
  get isTicketsPerRetrievalValid(): boolean {
    return this.configurationObj.ticketsPerRetrieval <= this.configurationObj.maxTicketCapacity
  }

  /**
   * Checks if the configuration name is available (unique).
   * @returns true if the name is unique among configurations, false otherwise.
   */
  get isConfigurationNameAvailable(): boolean {
    if (this.configurationObj.id === 0) {
      const configurations = this.configurationList();
      if (configurations.length === 0) {
        return true;
      } else {
        debugger;
        for (const config of configurations) {
          if (config.name.toLowerCase() === this.configurationObj.name.toLowerCase()) {
            return false;
          }
        }
        return true;
      }
    } else {
      return true
    }
  }

  /**
   * Resets the form to the default state.
   */
  onReset(): void {
    this.configurationObj = new Configuration();
  }

  /**
   * Saves or updates the configuration through the service.
   * Displays a snackbar notification upon success.
   */
  onSaveUpdate(): void {
    this.configurationService.saveUpdateConfiguration(this.configurationObj).subscribe((result: IApiResponseModel) => {
      if(this.configurationObj.id === 0) {
        this.showSnackbar("Configuration saved successfully!");
      } else {
        this.showSnackbar("Configuration updated successfully!")
      }
      this.loadConfigurations();
      this.configurationObj = new Configuration();
    })
  }

  /**
   * Populates the form with the selected configuration for editing.
   * @param obj The configuration object to edit.
   */
  onEdit(obj: Configuration): void {
    this.configurationObj = { ...obj };
  }

  /**
   * Deletes a configuration by its ID.
   * Displays a snackbar notification upon success or failure.
   * @param id The ID of the configuration to delete.
   */
  onDelete(id: number): void {
    this.configurationService.deleteConfiguration(id).subscribe((result: IApiResponseModel)=> {
      if (result.data) {
        this.showSnackbar("Configuration deleted successfully!");
      } else {
        this.showSnackbar("Error deleting configuration!")
      }
      
      this.loadConfigurations();
    })
  }

  /**
   * Displays a snackbar notification with a custom message.
   * @param message The message to display.
   */
  showSnackbar(message: string): void {
    this.snackBar.open(message, 'OK', {
      duration: 5000, // Auto dismiss after 3 seconds
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }
}
