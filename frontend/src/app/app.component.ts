import {Component, Output} from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {SystemInfo} from './model/systemInfo';
import {Alert} from './model/alert';
import {AlertMessageService} from './service/alertMessage.service';
import { environment } from '../environments/environment';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  private serverUrl = environment.backend + '/info';
  private stompClient;
  private alertMessageService: AlertMessageService;

  @Output() systemInfo = new SystemInfo();
  alerts = [];
  alert = new Alert('', '');


  constructor(alertMessageService: AlertMessageService) {
    this.initializeWebSocketConnection();
    this.alertMessageService = alertMessageService;
  }

  initializeWebSocketConnection() {
    const ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    const that = this;
    this.stompClient.connect({}, function(frame) {
      that.stompClient.subscribe('/topic/messages', (message) => {
        if (message.body) {
          const obj = JSON.parse(message.body);

          switch (obj.name) {
            case 'cpu' : that.systemInfo.cpuUsage = obj.value; break;
            case 'mem' : that.systemInfo.memUsage = obj.value; break;
            case 'swap' : that.systemInfo.swap = obj.value; break;
            case 'swapUsed' : that.systemInfo.swapUsage = obj.value; break;
            case 'wsRequests' : that.systemInfo.wsCounter = obj.value; break;
          }
        }
      });
      that.stompClient.subscribe('/topic/alerts', (message) => {
        if (message.body) {
          const obj = JSON.parse(message.body);
          that.alerts.push(obj);
        }
      });
    });
  }

  sendMsg() {
    this.alertMessageService.sendAlert(this.alert);
  }
}
