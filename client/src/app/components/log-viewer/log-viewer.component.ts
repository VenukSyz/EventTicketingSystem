import { AfterViewChecked, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { WebSocketService } from '../../services/web-socket.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-log-viewer',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './log-viewer.component.html',
  styleUrl: './log-viewer.component.css'
})
export class LogViewerComponent implements OnInit, OnDestroy, AfterViewChecked {
  logs: string[] = [];
  @ViewChild('logContainer') logContainer!: ElementRef;

  constructor(private webSocketService: WebSocketService) { }

  ngOnInit(): void {
    const savedLogs = sessionStorage.getItem('logs');
    if (savedLogs) {
      this.logs = JSON.parse(savedLogs);
    }
    this.webSocketService.connect((message: string) => {
      this.logs.push(message);
      sessionStorage.setItem('logs', JSON.stringify(this.logs));
    });
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom(); // Ensures the scroll stays at the bottom
  }

  scrollToBottom(): void {
    try {
      this.logContainer.nativeElement.scrollTop = this.logContainer.nativeElement.scrollHeight;
    } catch (err) {
      console.error('Failed to scroll:', err);
    }
  }

  resetTheLogger(): void {
    this.logs = [];
    sessionStorage.removeItem('logs'); // Clear logs from sessionStorage
  }

  ngOnDestroy(): void {
    this.webSocketService.disconnect();
  }
}