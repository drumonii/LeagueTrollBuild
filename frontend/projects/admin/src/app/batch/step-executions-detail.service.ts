import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { BatchStepExecution } from './batch-job';
import { AdminLogger } from '@admin-service/admin-logger.service';

@Injectable()
export class StepExecutionsDetailService {

  constructor(private logger: AdminLogger, private httpClient: HttpClient) {}

  getStepExecutions(jobInstanceId: number): Observable<BatchStepExecution[]> {
    this.logger.info(`GETing step executions for job instance id: ${jobInstanceId}`);
    return this.httpClient.get<BatchStepExecution[]>(`/job-instances/${jobInstanceId}/step-executions`)
      .pipe(
        map((stepExecutions) => stepExecutions.sort((s1, s2) => s1.id - s2.id)),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /job-instances/${jobInstanceId}/step-executions: ${JSON.stringify(error)}`);
          return of([]);
        })
      );
  }

}
