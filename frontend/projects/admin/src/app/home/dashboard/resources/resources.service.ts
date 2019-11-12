import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { forkJoin, Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { AdminLogger } from '@admin-service/admin-logger.service';
import { MetricsResponse } from '../actuator-metric-response';
import { HealthResponse } from '../actuator-health-response';

@Injectable()
export class ResourcesService {

  constructor(private logger: AdminLogger, private httpClient: HttpClient) {}

  getResourceConsumption(): Observable<ResourceConsumption> {
    return forkJoin([this.getCpuUsage(), this.getCpuCount(), this.getMemoryUsage(), this.getMemoryMax(), this.getDiskUsage()])
      .pipe(
        map((resources) => {
          if (resources.some(resource => resource === null)) {
            return {
              cpuUsage: null,
              memoryUsage: null,
              diskUsage: null
            };
          }
          return {
            cpuUsage: {
              percentage: resources[0],
              cpus: resources[1]
            },
            memoryUsage: {
              used: resources[2],
              max: resources[3]
            },
            diskUsage: resources[4]
          };
        })
      );
  }

  getCpuUsage(): Observable<number> {
    return this.httpClient.get<MetricsResponse>('/actuator/metrics/system.cpu.usage')
      .pipe(
        map((response) => {
          return response.measurements.find((measurement) => measurement.statistic === 'VALUE').value;
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /actuator/metrics/system.cpu.usage: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  getCpuCount(): Observable<number> {
    return this.httpClient.get<MetricsResponse>('/actuator/metrics/system.cpu.count')
      .pipe(
        map((response) => response.measurements.find((measurement) => measurement.statistic === 'VALUE').value),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /actuator/metrics/system.cpu.count: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  getMemoryUsage(): Observable<number> {
    return this.httpClient.get<MetricsResponse>('/actuator/metrics/jvm.memory.used')
      .pipe(
        map((response) => {
          return response.measurements.find((measurement) => measurement.statistic === 'VALUE').value;
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /actuator/metrics/jvm.memory.used: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  getMemoryMax(): Observable<number> {
    return this.httpClient.get<MetricsResponse>('/actuator/metrics/jvm.memory.max')
      .pipe(
        map((response) => {
          return response.measurements.find((measurement) => measurement.statistic === 'VALUE').value;
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /actuator/metrics/jvm.memory.max: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  getDiskUsage(): Observable<DiskUsage> {
    return this.httpClient.get<HealthResponse>('/actuator/health')
      .pipe(
        map((response) => {
          const totalDiskSpaceInBytes = response.components.diskSpace.details.total;
          const freeDiskSpaceInBytes = response.components.diskSpace.details.free;
          const usedDiskSpaceInBytes = totalDiskSpaceInBytes - freeDiskSpaceInBytes;
          return {
            used: usedDiskSpaceInBytes,
            max: totalDiskSpaceInBytes
          };
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /actuator/health: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}

export interface ResourceConsumption {
  cpuUsage: CpuUsage;
  memoryUsage: MemoryUsage;
  diskUsage: DiskUsage;
}

interface CpuUsage {
  percentage: number;
  cpus: number;
}

interface MemoryUsage {
  used: number;
  max: number;
}

interface DiskUsage {
  used: number;
  max: number;
}
