import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Logger } from '@ltb-service/logger.service';
import { Build } from '@ltb-model/build';

@Injectable()
export class BuildsService {

  constructor(private logger: Logger, private httpClient: HttpClient) {}

  getBuild(buildId: number): Observable<Build> {
    this.logger.info('GETing build', buildId);
    return this.httpClient.get<Build>(`/builds/${buildId}`)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.logger.warn(`Caught error while GETing build ${buildId}: ${JSON.stringify(error)}`);
          if (error.status === 404) {
            return of(null);
          }
          return of(new Build());
        })
      );
  }

  countBuilds(): Observable<number> {
    this.logger.info('GETing count of builds');
    return this.httpClient.get<number>('/builds/count')
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing count of builds ${JSON.stringify(error)}`);
          return of(0);
        })
     );
  }

}
