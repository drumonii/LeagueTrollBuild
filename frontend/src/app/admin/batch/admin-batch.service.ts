import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { Logger } from '@service/logger.service';
import { Paginated } from '@admin-model/paginated';
import { BatchJobInstance } from '@admin-model/batch-job-instance';
import { BatchStepExecution } from '@admin-model/batch-step-execution';
import { BatchJobExecution } from '@admin-model/batch-job-execution';
import { PageRequest } from '@admin-model/page-request';

@Injectable()
export class AdminBatchService {

  constructor(private logger: Logger, private httpClient: HttpClient) {}

  getBatchJobInstances(pageRequest: PageRequest): Observable<Paginated<BatchJobInstance>> {
    let params = new HttpParams();
    if (pageRequest.hasOwnProperty('page')) {
      params = params.append('page', pageRequest.page.toString());
    }
    if (pageRequest.hasOwnProperty('size')) {
      params = params.append('size', pageRequest.size.toString());
    }
    if (pageRequest.hasOwnProperty('sort')) {
      for (const sort of pageRequest.sort) {
        params = params.append('sort', sort);
      }
    }
    const options = {
      params
    };
    this.logger.info(`GETing job instances with page request: ${params}`);
    return this.httpClient.get<Paginated<BatchJobInstance>>('/job-instances', options)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /job-instances: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  getStepExecutions(jobInstanceId: number): Observable<BatchStepExecution[]> {
    this.logger.info(`GETing step executions for job instance id: ${jobInstanceId}`);
    return this.httpClient.get<BatchStepExecution[]>(`/job-instances/${jobInstanceId}/step-executions`)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /job-instances/${jobInstanceId}/step-executions: ${JSON.stringify(error)}`);
          return of([]);
        })
      );
  }

  hasFailedAllRetrievalsJob(minutesAgo: number): Observable<boolean> {
    const params = new HttpParams()
      .set('minutes', minutesAgo.toString());
    const options = {
      params
    };
    this.logger.info('GETing has failed all retrievals job');
    return this.httpClient.get<any>('/job-instances/has-failed-all-retrievals-job', options)
      .pipe(
        map((response) => response.hasFailedAllRetrievalsJob),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /job-instances/has-failed-all-retrievals-job: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  restartAllRetrievalsJob(): Observable<BatchJobExecution> {
    this.logger.info('POSTing all retrievals manual job restart');
    return this.httpClient.post<BatchJobExecution>('/job-instances/restart', {})
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while POSTing /job-instances/restart: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}
