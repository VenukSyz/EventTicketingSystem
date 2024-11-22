import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ConfigurationService } from '../../services/configuration.service';
import { Configuration } from '../../model/class/configuration';
import { IApiResponseModel } from '../../model/interface/api';

@Component({
  selector: 'app-configuration-form',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './configuration-form.component.html',
  styleUrl: './configuration-form.component.css'
})
export class ConfigurationFormComponent implements OnInit{

  configurationService: ConfigurationService = inject(ConfigurationService);
  configurationObj: Configuration = new Configuration();
  configurationList = signal<Configuration[]>([])

  ngOnInit(): void {
      this.loadConfigurations();
  }

  loadConfigurations() {
    this.configurationService.getAllConfigurations().subscribe((result: IApiResponseModel) => {
      debugger;
      this.configurationList.set(result.data);
    });
  }

  get isTotalTicketsValid() : boolean {
    return this.configurationObj.totalTickets <= this.configurationObj.maxTicketCapacity;
  }

  get isTicketPerReleaseValid() : boolean {
    return this.configurationObj.ticketsPerRelease <= this.configurationObj.maxTicketCapacity - this.configurationObj.totalTickets;
  }

  get isTicketsPerRetrievalValid() : boolean {
    return this.configurationObj.ticketsPerRetrieval <= this.configurationObj.maxTicketCapacity
  }

}
