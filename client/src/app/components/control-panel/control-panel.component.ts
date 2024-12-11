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
import { TimelineChartComponent } from '../timeline-chart/timeline-chart.component';

/**
 * ControlPanelComponent
 * Manages the control panel functionality, including starting, stopping, resetting the system,
 * and updating the UI components such as log viewer, ticket status, and timeline chart.
 */
@Component({
  selector: 'app-control-panel',
  standalone: true,
  imports: [FormsModule, CommonModule, LogViewerComponent, TicketStatusComponent, MatProgressBarModule, TimelineChartComponent],
  templateUrl: './control-panel.component.html',
  styleUrl: './control-panel.component.css'
})
export class ControlPanelComponent implements OnInit{

  configurationService: ConfigurationService = inject(ConfigurationService);
  controlPanelService: ControlPanelService = inject(ControlPanelService);

  /** List of configurations fetched from the service. */
  configurationList = signal<Configuration[]>([]);

  /** The currently selected control panel configuration object. */
  controlPanelObj: ControlPanel = new ControlPanel();

  /** Indicates whether the loader is active. */
  isLoader: boolean = true;

  /** Flag to manage the current button state (e.g., start, stop, reset). */
  btnFlag: number = 0;

  /** Total number of tickets released in the system. */
  totalTickets: number = 0;

  /** Maximum ticket capacity for the selected configuration. */
  max: number = 0;

  /** Number of tickets processed or started. */
  start: number = 0;

  /** Current progress percentage based on tickets processed. */
  progress: number = 0;

  /** Reference to the log viewer component. */
  @ViewChild('logViewer') logViewer!: LogViewerComponent;

  /** Reference to the ticket status component. */
  @ViewChild('ticketStatus') ticketStatus!: TicketStatusComponent;

  /** Reference to the timeline chart component. */
  @ViewChild('timelineChart') timelineChart!: TimelineChartComponent;

  /**
   * Initializes the component with required services.
   * @param snackBar Service to display notifications to the user.
   */
  constructor(private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.updateFromSessionStorage('btnFlag');
    this.updateFromSessionStorage('totalTickets');
    this.updateFromSessionStorage('max');
    this.updateFromSessionStorage('start');
    this.updateFromSessionStorage('progress');
    this.loadConfigurations();
    this.loadControlPanelFromSession();
  }

   /**
   * Fetches all configurations from the configuration service.
   */
  loadConfigurations(): void {
    this.configurationService.getAllConfigurations().subscribe((result: IApiResponseModel) => {
      this.configurationList.set(result.data);
      this.isLoader = false;
    });
  }

  /**
   * Starts or resumes the system based on the current state.
   * Updates the progress bar and saves data to session storage.
   */
  onStart(): void {
    if (this.btnFlag === 0) {
      this.controlPanelService.startSystem(this.controlPanelObj).subscribe((result: IApiResponseModel) => {
        this.showSnackbar(result.data);
      });
      const selectedConfig = this.configurationList().find(config => config.id == this.controlPanelObj.configId);
      this.initializeProgressBar(selectedConfig?.maxTicketCapacity, selectedConfig?.totalTickets);
      this.saveControlPanelToSession();
    } else {
      this.controlPanelService.resumeSystem(this.controlPanelObj).subscribe((result: IApiResponseModel) => {
        this.showSnackbar(result.data);
      })
      this.saveControlPanelToSession();
    }
    this.btnFlag = 1;
    this.setSessionStorage('btnFlag', this.btnFlag);
    this.setSessionStorageForProgressBar();
  }

   /**
   * Stops the system and updates the button flag state.
   */
  onStop(): void {
    this.btnFlag = 2;
    this.controlPanelService.stopSystem().subscribe((result: IApiResponseModel) => {
      this.showSnackbar(result.data);
    });
    sessionStorage.setItem('btnFlag',this.btnFlag.toString());
  }

  /**
   * Resets the control panel state and clears session storage.
   */
  onReset(): void {
    this.btnFlag = 0;
    this.totalTickets = 0;
    this.max = 0;
    this.start = 0;
    this.progress = 0;
    this.setSessionStorageForProgressBar();
    this.logViewer.resetTheLogger();
    this.ticketStatus.resetTicketStatus();
    this.timelineChart.resetChart();
    sessionStorage.removeItem('controlPanelObj');
    this.showSnackbar("The system reset");
    sessionStorage.setItem('btnFlag',this.btnFlag.toString());
  }

  /**
   * Determines if a configuration is selected.
   * 
   * @returns {boolean} `true` if a configuration is selected; otherwise, `false`.
   */
  get isConfigurationSelected() : boolean {
    return this.controlPanelObj.configId === 0 ? false : true;
  }

  /**
   * Displays a snackbar notification with a custom message.
   * 
   * @param {string} message The message to display in the snackbar.
   */
  showSnackbar(message: string): void {
    this.snackBar.open(message, 'OK', {
      duration: 5000, // Auto dismiss after 3 seconds
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }

  /**
   * Initializes the progress bar based on maximum ticket capacity and starting value.
   * 
   * @param {number | undefined} max The maximum ticket capacity.
   * @param {number | undefined} start The starting number of tickets.
   */
  initializeProgressBar(max: number | undefined, start: number | undefined) {
    if (max !== undefined && start !== undefined) {
      this.max = max;
      this.totalTickets = start;
      this.start = start;
      this.progress = Math.floor((start / max) * 100)
    }
  }

  /**
   * Updates the progress bar and timeline chart based on the event data.
   * 
   * @param {Object} event An object containing tickets and soldOutTickets values.
   */
  updateProgress(event: {tickets: number, soldOutTickets: number}): void {
    this.start = event.tickets + this.totalTickets;
    this.progress = Math.floor((this.start / this.max) * 100);
    this.setSessionStorage('start', this.start);
    this.setSessionStorage('progress', this.progress);
    if (this.timelineChart) {
      this.timelineChart.updateChart(event.soldOutTickets);
    }
  }

  /**
   * Saves a key-value pair to session storage.
   * 
   * @param {string} name The key name.
   * @param {number} obj The value to store.
   */
  setSessionStorage(name: string, obj: number) {
    sessionStorage.setItem(name, obj.toString());
  }

  /**
   * Saves progress bar state variables to session storage.
   */
  setSessionStorageForProgressBar(): void {
    this.setSessionStorage('totalTickets', this.totalTickets);
    this.setSessionStorage('max', this.max);
    this.setSessionStorage('start', this.start);
    this.setSessionStorage('progress', this.progress);
  }

  /**
   * Updates class properties with values from session storage for the given key.
   * 
   * @param {string} key The session storage key to fetch.
   */
  updateFromSessionStorage(key: string): void {
    const savedValue = sessionStorage.getItem(key);
    if (savedValue !== null) {
      (this as any)[key] = parseInt(savedValue, 10);
    }
  }

  /**
   * Saves the current control panel object to session storage.
   */
  saveControlPanelToSession(): void {
    sessionStorage.setItem('controlPanelObj', JSON.stringify(this.controlPanelObj));
  }

  /**
   * Loads the control panel object from session storage if it exists.
   */
  loadControlPanelFromSession(): void {
    const controlPanelJSON = sessionStorage.getItem('controlPanelObj');
    if (controlPanelJSON) {
      this.controlPanelObj = JSON.parse(controlPanelJSON);
    }
  }
}
