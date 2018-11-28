import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {tap} from 'rxjs/operators';

@Injectable()
export class InitialService {

  constructor(private http: HttpClient) { }

  getInitialData() {
    return this.http.get(environment.backend + '/initial')
      .pipe(tap(
        data => { console.log('Data: ');  console.log(data); },
        error => console.log('Error: ' + error)
      ));
  }

}
