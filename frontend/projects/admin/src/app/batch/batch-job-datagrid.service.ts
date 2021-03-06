import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { AdminLogger } from '@admin-service/admin-logger.service';
import { PageRequest, Paginated } from './datagrid';
import { BatchJobExecution, BatchJobInstance } from './batch-job';

@Injectable()
export class BatchJobDatagridService {

  constructor(private logger: AdminLogger, private httpClient: HttpClient) {}

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
    if (pageRequest.hasOwnProperty('filters')) {
      for (const filter of pageRequest.filters) {
        params = params.append(filter.property, filter.value);
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
