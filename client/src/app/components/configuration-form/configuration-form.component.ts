import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ConfigurationService } from '../../services/configuration.service';
import { Configuration } from '../../model/class/Configuration';
import { IApiResponseModel } from '../../model/interface/api';
import { MatSnackBar } from '@angular/material/snack-bar';

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
  configurationList = signal<Configuration[]>([])
  isLoader: boolean = true;
  btnText: string = 'Save';

  constructor(private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.loadConfigurations();
  }

  loadConfigurations(): void {
    this.configurationService.getAllConfigurations().subscribe((result: IApiResponseModel) => {
      this.configurationList.set(result.data);
      this.isLoader = false;
    });
  }

  get isTotalTicketsValid(): boolean {
    return this.configurationObj.totalTickets <= this.configurationObj.maxTicketCapacity;
  }

  get isTicketPerReleaseValid(): boolean {
    return this.configurationObj.ticketsPerRelease <= this.configurationObj.maxTicketCapacity - this.configurationObj.totalTickets;
  }

  get isTicketsPerRetrievalValid(): boolean {
    return this.configurationObj.ticketsPerRetrieval <= this.configurationObj.maxTicketCapacity
  }

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


  onReset(): void {
    this.configurationObj = new Configuration();
  }

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

  onEdit(obj: Configuration): void {
    this.configurationObj = { ...obj };
  }

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

  showSnackbar(message: string): void {
    this.snackBar.open(message, 'OK', {
      duration: 5000, // Auto dismiss after 3 seconds
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }

}
