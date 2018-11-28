import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Alert} from '../model/alert';
import {tap} from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable()
export class AlertMessageService {
  constructor(private http: HttpClient) { }

  sendAlert(alertMessage: Alert) {
    this.http.post<Alert>(environment.backend + '/alert', alertMessage)
      .pipe(tap(
        data => console.log('Data: ' +  data),
        error => console.log('Error: ' + error)
      )).subscribe();
  }
}
