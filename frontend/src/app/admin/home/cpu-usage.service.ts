import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { ActuatorResponse } from '@admin-model/actuator-response';
import { AdminLogger } from '@admin-service/admin-logger.service';
import { AdminService } from '@admin-service/admin-service';

@Injectable()
export class CpuUsageService extends AdminService {

  constructor(private logger: AdminLogger, private httpClient: HttpClient) {
    super();
  }

  getCpuUsagePerc(): Observable<string> {
    const headers = this.getBaseHttpHeaders();
    const options = {
      headers
    };
    return this.httpClient.get<ActuatorResponse>('/actuator/metrics/system.cpu.usage', options)
      .pipe(
        map((response) => {
          const cpuUsage = response.measurements.find((measurement) => measurement.statistic === 'VALUE').value;
          return (cpuUsage * 100).toFixed(3);
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing cpu usage actuator data: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  getCpuCount(): Observable<number> {
    const headers = this.getBaseHttpHeaders();
    const options = {
      headers
    };
    return this.httpClient.get<ActuatorResponse>('/actuator/metrics/system.cpu.count', options)
      .pipe(
        map((response) => response.measurements.find((measurement) => measurement.statistic === 'VALUE').value),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing cpu count actuator data: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}

export interface CpuUsage {
  percentage: string;
  cpus: number;
}
