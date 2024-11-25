import { Component, inject, OnInit, signal } from '@angular/core';
import { ConfigurationService } from '../../services/configuration.service';
import { Configuration } from '../../model/class/Configuration';
import { IApiResponseModel } from '../../model/interface/api';

@Component({
  selector: 'app-control-panel',
  standalone: true,
  imports: [],
  templateUrl: './control-panel.component.html',
  styleUrl: './control-panel.component.css'
})
export class ControlPanelComponent implements OnInit{
  configurationService: ConfigurationService = inject(ConfigurationService);
  configurationList = signal<Configuration[]>([])
  isLoader: boolean = true;

  ngOnInit(): void {
      this.loadConfigurations();
  }

  loadConfigurations() {
    this.configurationService.getAllConfigurations().subscribe((result: IApiResponseModel) => {
      this.configurationList.set(result.data);
      this.isLoader = false;
    });
  }
}
