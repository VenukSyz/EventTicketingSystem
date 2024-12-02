import { Component, inject, OnInit, signal, ViewChild } from '@angular/core';
import { ConfigurationService } from '../../services/configuration.service';
import { Configuration } from '../../model/class/Configuration';
import { IApiResponseModel } from '../../model/interface/api';
import { FormsModule } from '@angular/forms';
import { ControlPanel } from '../../model/class/ControlPanel';
import { CommonModule } from '@angular/common';
import { ControlPanelService } from '../../services/control-panel.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LogViewerComponent } from '../log-viewer/log-viewer.component';
import { TicketStatusComponent } from '../ticket-status/ticket-status.component';

@Component({
  selector: 'app-control-panel',
  standalone: true,
  imports: [FormsModule, CommonModule, LogViewerComponent, TicketStatusComponent],
  templateUrl: './control-panel.component.html',
  styleUrl: './control-panel.component.css'
})
export class ControlPanelComponent implements OnInit{
  configurationService: ConfigurationService = inject(ConfigurationService);
  controlPanelService: ControlPanelService = inject(ControlPanelService)
  configurationList = signal<Configuration[]>([])
  controlPanelObj: ControlPanel = new ControlPanel();
  isLoader: boolean = true;
  btnFlag: number = 0;
  @ViewChild('logViewer') logViewer!: LogViewerComponent;
  @ViewChild('ticketStatus') ticketStatus!: TicketStatusComponent;

  constructor(private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    const savedBtnFlag = sessionStorage.getItem('btnFlag');
    if (savedBtnFlag !== null) {
      this.btnFlag = parseInt(savedBtnFlag, 10);
    }
    this.loadConfigurations();
  }

  loadConfigurations(): void {
    this.configurationService.getAllConfigurations().subscribe((result: IApiResponseModel) => {
      this.configurationList.set(result.data);
      this.isLoader = false;
    });
  }

  onStart(): void {
    if (this.btnFlag === 0) {
      this.controlPanelService.startSystem(this.controlPanelObj).subscribe((result: IApiResponseModel) => {
        this.showSnackbar(result.data);
      });
    } else {
      this.controlPanelService.resumeSystem().subscribe((result: IApiResponseModel) => {
        this.showSnackbar(result.data);
      })
    }
    this.btnFlag = 1;
    sessionStorage.setItem('btnFlag',this.btnFlag.toString());
  }

  onStop(): void {
    this.btnFlag = 2;
    this.controlPanelService.stopSystem().subscribe((result: IApiResponseModel) => {
      this.showSnackbar(result.data);
    });
    sessionStorage.setItem('btnFlag',this.btnFlag.toString());
  }

  onReset(): void {
    this.btnFlag = 0;
    this.logViewer.resetTheLogger();
    this.ticketStatus.resetTicketStatus();
    this.showSnackbar("The system reset");
    sessionStorage.setItem('btnFlag',this.btnFlag.toString());
  }

  get isConfigurationSelected() : boolean {
    return this.controlPanelObj.id === 0 ? false : true;
  }

  showSnackbar(message: string): void {
    this.snackBar.open(message, 'OK', {
      duration: 5000, // Auto dismiss after 3 seconds
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }
}
