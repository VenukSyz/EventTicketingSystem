import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { TicketStatusService } from './ticket-status.service';
import { environment } from '../../environments/environment.development';
import { Constant } from '../constant/Constant';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  /** 
   * The STOMP client instance used for communication over WebSocket.
   * Initialized as null, will be set once connected to the WebSocket.
   */
  private stompClient: Client | null = null;

  /**
   * Creates an instance of WebSocketService.
   * 
   * @param ticketStatusService - The service used to update and manage the ticket system status.
   */
  constructor(private ticketStatusService: TicketStatusService) {}

  /**
   * Establishes a connection to the WebSocket server using SockJS and STOMP protocol.
   * Subscribes to two topics: one for log messages and another for status updates.
   * 
   * @param logCallback - A callback function that will be invoked with log messages received from the WebSocket.
   */
  connect(logCallback: (message: string) => void): void {
    // Creates a SockJS instance
    const socket = new SockJS(environment.WEBSOCKET_URL);

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
      this.stompClient?.subscribe(Constant.WEBSOCKET_PATHS.LOG_PATH, (message) => {
        logCallback(message.body);
      });

      // Subscribe to structured status updates and share it using SystemStatusService
      this.stompClient?.subscribe(Constant.WEBSOCKET_PATHS.STATUS_PATH, (message) => {
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

   /**
   * Disconnects from the WebSocket server and deactivates the STOMP client.
   */
  disconnect(): void {
    this.stompClient?.deactivate();
    console.log("Disconnected from WebScoket");
  }
}
