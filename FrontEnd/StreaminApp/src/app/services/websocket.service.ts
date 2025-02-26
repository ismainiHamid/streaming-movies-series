import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {NotificationResponse} from '../models/notification/notification-response';
import {CompatClient, Message, Stomp, StompSubscription} from '@stomp/stompjs';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private stompClient: CompatClient | undefined;
  private subscription: StompSubscription | undefined;
  private messageSubject: Subject<NotificationResponse> = new Subject<NotificationResponse>();
  private socketUrl: string = 'http://localhost:7070/cinewave/api/notification';

  public connect(): void {
    this.stompClient = Stomp.client(this.socketUrl);

    this.stompClient.connect({}, (frame: any): void => {
      console.log('STOMP Connected: ', frame);
      if (this.stompClient) {
        this.subscription = this.stompClient.subscribe('/topic/notify/progress/movies', (message: Message): void => {
          const notificationResponse: NotificationResponse = JSON.parse(message.body);
          this.messageSubject.next(notificationResponse);
        });
      }
    }, (error: Error): void => {
      console.error('STOMP connection error: ', error);
      this.reconnect();
    });
    this.stompClient.onWebSocketClose = (): void => {
      console.warn('WebSocket closed, reconnecting...');
      this.reconnect();
    };
  }

  public getMessages(): Observable<NotificationResponse> {
    return this.messageSubject.asObservable();
  }

  public stopConnection(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
      console.log('Unsubscribed from topic');
    }
    if (this.stompClient) {
      this.stompClient.disconnect((): void => {
        console.log('Disconnected from WebSocket');
      });
    }
  }

  private reconnect(): void {
    console.info('Reconnecting to STOMP...');
    setTimeout((): void => {
      if (this.stompClient && !this.stompClient.connected) {
        this.connect();
      }
    }, 3000);
  }
}
