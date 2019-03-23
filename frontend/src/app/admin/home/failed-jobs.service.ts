import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { BatchJobInstance } from '@admin-model/batch-job-instance';
import { Paginated } from '@admin-model/paginated';
import { AdminLogger } from '@admin-service/admin-logger.service';
import { AdminService } from '@admin-service/admin-service';

@Injectable()
export class FailedJobsService extends AdminService {

  constructor(private logger: AdminLogger, private httpClient: HttpClient) {
    super();
  }

  getFailedJobs(): Observable<number> {
    const params = new HttpParams()
      .set('jobExecution.status', 'FAILED');
    const headers = this.getBaseHttpHeaders();
    const options = {
      params,
      headers
    };
    return this.httpClient.get<Paginated<BatchJobInstance>>('/job-instances', options)
      .pipe(
        map((response) => response.numberOfElements),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /job-instances: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}
