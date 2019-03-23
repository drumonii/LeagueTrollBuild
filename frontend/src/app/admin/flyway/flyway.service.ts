import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { FlywayMigration, FlywayResponse } from './flyway-response';
import { AdminLogger } from '@admin-service/admin-logger.service';
import { AdminService } from '@admin-service/admin-service';

@Injectable()
export class FlywayService extends AdminService {

  constructor(private logger: AdminLogger, private httpClient: HttpClient) {
    super();
  }

  getFlyway(): Observable<FlywayMigration[]> {
    const headers = this.getBaseHttpHeaders();
    const options = {
      headers
    };
    return this.httpClient.get<FlywayResponse>('/actuator/flyway', options)
      .pipe(
        map((response) => response.contexts.application.flywayBeans.flyway.migrations),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing flyway actuator data: ${JSON.stringify(error)}`);
          return of([]);
        })
      );
  }

}
