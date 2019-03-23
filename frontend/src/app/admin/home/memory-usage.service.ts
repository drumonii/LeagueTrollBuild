import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { ActuatorResponse } from '@admin-model/actuator-response';
import { AdminLogger } from '@admin-service/admin-logger.service';
import { AdminService } from '@admin-service/admin-service';

@Injectable()
export class MemoryUsageService extends AdminService {

  constructor(private logger: AdminLogger, private httpClient: HttpClient) {
    super();
  }

  getMemoryUsage(): Observable<string> {
    const headers = this.getBaseHttpHeaders();
    const options = {
      headers
    };
    return this.httpClient.get<ActuatorResponse>('/actuator/metrics/jvm.memory.used', options)
      .pipe(
        map((response) => {
          const memoryInBytes = response.measurements.find((measurement) => measurement.statistic === 'VALUE').value;
          const memoryInMegabytes = memoryInBytes / 1_000_000;
          return memoryInMegabytes.toFixed(2);
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing JVM memory used actuator data: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}
