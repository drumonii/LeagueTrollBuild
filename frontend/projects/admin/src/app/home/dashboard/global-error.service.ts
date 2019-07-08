import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { ActuatorResponse } from './actuator-response';
import { AdminLogger } from '@admin-service/admin-logger.service';

@Injectable()
export class GlobalErrorService {

  constructor(private logger: AdminLogger, private httpClient: HttpClient) {}

  getGlobalErrors(): Observable<number> {
    return this.httpClient.get<ActuatorResponse>('/actuator/metrics/tomcat.global.error')
      .pipe(
        map((response) => response.measurements.find((measurement) => measurement.statistic === 'COUNT').value),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing tomcat global error actuator data: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}
