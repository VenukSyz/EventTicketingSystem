import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { TicketStatusService } from './ticket-status.service';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient: Client | null = null;

  constructor(private ticketStatusService: TicketStatusService) {}

  connect(logCallback: (message: string) => void): void {
    // Creates a SockJS instance
    const socket = new SockJS("http://localhost:8080/websocket");

    // Create a STOMP client using @stomp/stompjs
    this.stompClient = new Client({
      webSocketFactory: () => socket as any,
      reconnectDelay: 5000,
      debug: (msg) => {
        console.log(msg);
      },
    });

    this.stompClient.onConnect = () => {
      console.log('Connected to WebSocket');
      this.stompClient?.subscribe("/topic/logs", (message) => {
        logCallback(message.body);
      });

      // Subscribe to structured status updates and share it using SystemStatusService
      this.stompClient?.subscribe('/topic/status', (message) => {
        const status = JSON.parse(message.body);
        console.log('Received from WebSocket:', status);
        this.ticketStatusService.updateSystemStatus(status);
      });
    };

    this.stompClient.onStompError = (frame) => {
      console.error("Broker reported error: ", frame.headers['message']);
      console.error("Additional details: ", frame.body);
    };

    this.stompClient.activate();
  }

  disconnect(): void {
    this.stompClient?.deactivate();
    console.log("Disconnected from WebScoket");
  }
}
