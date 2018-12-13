import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { ActuatorResponse } from '@admin-model/actuator-response';
import { Logger } from '@service/logger.service';

@Injectable()
export class ServletErrorService {

  constructor(private logger: Logger, private httpClient: HttpClient) {}

  getServletErrors(): Observable<Number> {
    return this.httpClient.get<ActuatorResponse>('/admin/actuator/metrics/tomcat.servlet.error')
      .pipe(
        map((response) => response.measurements.find((measurement) => measurement.statistic === 'COUNT').value),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /admin/actuator/metrics/tomcat.servlet.error: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}