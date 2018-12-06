import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { FlywayMigration, FlywayResponse } from './flyway-response';
import { Logger } from '@service/logger.service';

@Injectable()
export class FlywayService {

  constructor(private logger: Logger, private httpClient: HttpClient) {}

  getFlyway(): Observable<FlywayMigration[]> {
    return this.httpClient.get<FlywayResponse>('/admin/actuator/flyway')
      .pipe(
        map((response) => response.contexts.application.flywayBeans.flyway.migrations),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /admin/actuator/flyway: ${JSON.stringify(error)}`);
          return of([]);
        })
      );
  }

}
