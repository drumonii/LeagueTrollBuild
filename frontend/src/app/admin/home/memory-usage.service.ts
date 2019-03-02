import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { ActuatorResponse } from '@admin-model/actuator-response';
import { AdminLogger } from '@admin-service/admin-logger.service';

@Injectable()
export class MemoryUsageService {

  constructor(private logger: AdminLogger, private httpClient: HttpClient) {}

  getMemoryUsage(): Observable<string> {
    return this.httpClient.get<ActuatorResponse>('/admin/actuator/metrics/jvm.memory.used')
      .pipe(
        map((response) => {
          const memoryInBytes = response.measurements.find((measurement) => measurement.statistic === 'VALUE').value;
          const memoryInMegabytes = memoryInBytes / 1_000_000;
          return memoryInMegabytes.toFixed(2);
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /admin/actuator/metrics/jvm.memory.used: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}
