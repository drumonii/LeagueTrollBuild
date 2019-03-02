import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { ActuatorResponse } from '@admin-model/actuator-response';
import { AdminLogger } from '@admin-service/admin-logger.service';

@Injectable()
export class CpuUsageService {

  constructor(private logger: AdminLogger, private httpClient: HttpClient) {}

  getCpuUsagePerc(): Observable<string> {
    return this.httpClient.get<ActuatorResponse>('/admin/actuator/metrics/system.cpu.usage')
      .pipe(
        map((response) => {
          const cpuUsage = response.measurements.find((measurement) => measurement.statistic === 'VALUE').value;
          return (cpuUsage * 100).toFixed(3);
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /admin/actuator/metrics/system.cpu.usage: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  getCpuCount(): Observable<number> {
    return this.httpClient.get<ActuatorResponse>('/admin/actuator/metrics/system.cpu.count')
      .pipe(
        map((response) => response.measurements.find((measurement) => measurement.statistic === 'VALUE').value),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /admin/actuator/metrics/system.cpu.count: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}

export interface CpuUsage {
  percentage: string;
  cpus: number;
}
