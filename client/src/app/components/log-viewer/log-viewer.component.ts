import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { WebSocketService } from '../../services/web-socket.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-log-viewer',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './log-viewer.component.html',
  styleUrl: './log-viewer.component.css'
})
export class LogViewerComponent implements OnInit, OnDestroy {
  /** Array to store log messages received via WebSocket. */
  logs: string[] = [];

  /** Reference to the log container element in the template. */
  @ViewChild('logContainer') logContainer!: ElementRef;

  /**
   * Initializes the WebSocket connection and loads logs from session storage if available.
   * @param webSocketService - Service for managing WebSocket connections.
   */
  constructor(private webSocketService: WebSocketService) { }

  ngOnInit(): void {
    const savedLogs = sessionStorage.getItem('logs');
    if (savedLogs) {
      this.logs = JSON.parse(savedLogs);
    }
    this.webSocketService.connect((message: string) => {
      this.logs.push(message);
      sessionStorage.setItem('logs', JSON.stringify(this.logs));
      this.scrollToBottom();
    });
  }

  /**
   * Resets the logger by clearing all logs and removing them from session storage.
   */
  resetTheLogger(): void {
    this.logs = [];
    sessionStorage.removeItem('logs'); // Clear logs from sessionStorage
  }

  /**
   * Scrolls the log container to the bottom to show the latest log message.
   */
  private scrollToBottom(): void {
    setTimeout(() => {
      if (this.logContainer && this.logContainer.nativeElement) {
        this.logContainer.nativeElement.scrollTop = this.logContainer.nativeElement.scrollHeight;
      }
    }, 0);
  }

  ngOnDestroy(): void {
    this.webSocketService.disconnect();
  }
}
