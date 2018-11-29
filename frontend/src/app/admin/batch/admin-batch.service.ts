import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Logger } from '@service/logger.service';
import { Paginated } from '@admin-model/paginated';
import { BatchJobInstance } from '@admin-model/batch-job-instance';
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
      params: params
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

}
