import { Component, EventEmitter, inject, OnInit, Output, signal, ViewChild } from '@angular/core';
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
import { MatProgressBarModule } from '@angular/material/progress-bar';

@Component({
  selector: 'app-control-panel',
  standalone: true,
  imports: [FormsModule, CommonModule, LogViewerComponent, TicketStatusComponent, MatProgressBarModule],
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
  totalTickets: number = 0;
  max: number = 0;
  start: number = 0;
  progress: number = 0;
  @ViewChild('logViewer') logViewer!: LogViewerComponent;
  @ViewChild('ticketStatus') ticketStatus!: TicketStatusComponent;

  constructor(private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.updateFromSessionStorage('btnFlag');
    this.updateFromSessionStorage('totalTickets');
    this.updateFromSessionStorage('max');
    this.updateFromSessionStorage('start');
    this.updateFromSessionStorage('progress');
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
      const selectedConfig = this.configurationList().find(config => config.id == this.controlPanelObj.id);
      this.initializeProgressBar(selectedConfig?.maxTicketCapacity, selectedConfig?.totalTickets);
    } else {
      this.controlPanelService.resumeSystem().subscribe((result: IApiResponseModel) => {
        this.showSnackbar(result.data);
      })
    }
    this.btnFlag = 1;
    this.setSessionStorage('btnFlag', this.btnFlag);
    this.setSessionStorageForProgressBar();
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
    this.totalTickets = 0;
    this.max = 0;
    this.start = 0;
    this.progress = 0;
    this.setSessionStorageForProgressBar();
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

  initializeProgressBar(max: number | undefined, start: number | undefined) {
    if (max !== undefined && start !== undefined) {
      this.max = max;
      this.totalTickets = start;
      this.start = start;
      this.progress = Math.floor((start / max) * 100)
    }
  }

  updateProgress(tickets: number): void {
    this.start = tickets + this.totalTickets;
    this.progress = Math.floor((this.start / this.max) * 100);
    this.setSessionStorage('start', this.start)
    this.setSessionStorage('progress', this.progress)
  }

  setSessionStorage(name: string, obj: number) {
    sessionStorage.setItem(name, obj.toString());
  }

  setSessionStorageForProgressBar(): void {
    this.setSessionStorage('totalTickets', this.totalTickets);
    this.setSessionStorage('max', this.max);
    this.setSessionStorage('start', this.start);
    this.setSessionStorage('progress', this.progress);
  }

  updateFromSessionStorage(key: string): void {
    const savedValue = sessionStorage.getItem(key);
    if (savedValue !== null) {
      (this as any)[key] = parseInt(savedValue, 10);
    }
  }
}
