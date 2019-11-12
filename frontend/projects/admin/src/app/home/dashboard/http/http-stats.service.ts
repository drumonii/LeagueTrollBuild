import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { AdminLogger } from '@admin-service/admin-logger.service';
import { ActuatorHttpResponse } from '../actuator-http-response';

@Injectable()
export class HttpStatsService {

  constructor(private logger: AdminLogger, private httpClient: HttpClient) {}

  getHttpStats(): Observable<HttpStats> {
    return this.httpClient.get<ActuatorHttpResponse>('/actuator/metrics/http.server.requests')
      .pipe(
        map((response) => ({
          measurements: {
            count: response.measurements.find(measurement => measurement.statistic === 'COUNT').value,
            max: response.measurements.find(measurement => measurement.statistic === 'MAX').value
          },
          exceptions: response.availableTags.find(availableTag => availableTag.tag === 'exception').values
            .filter((exception) => exception !== 'None'),
          uris: response.availableTags.find(availableTag => availableTag.tag === 'uri').values
            .filter((uri) => uri !== '/**')
            .filter((uri) => uri !== 'root'),
          statuses: response.availableTags.find(availableTag => availableTag.tag === 'status').values
        })),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /actuator/metrics/http.server.requests: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}

export interface HttpStats {
  measurements: HttpMeasurement;
  exceptions: string[];
  uris: string[];
  statuses: string[];
}

interface HttpMeasurement {
  count: number;
  max: number;
}
