import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { ActuatorResponse } from '@admin-model/actuator-response';
import { AdminLogger } from '@admin-service/admin-logger.service';
import { AdminService } from '@admin-service/admin-service';

@Injectable()
export class GlobalErrorService extends AdminService {

  constructor(private logger: AdminLogger, private httpClient: HttpClient) {
    super();
  }

  getGlobalErrors(): Observable<number> {
    const headers = this.getBaseHttpHeaders();
    const options = {
      headers
    };
    return this.httpClient.get<ActuatorResponse>('/actuator/metrics/tomcat.global.error', options)
      .pipe(
        map((response) => response.measurements.find((measurement) => measurement.statistic === 'COUNT').value),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing tomcat global error actuator data: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}
