import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Logger } from '@service/logger.service';
import { Version } from '@model/version';

@Injectable({
  providedIn: 'root'
})
export class VersionsService {

  constructor(private logger: Logger, private httpClient: HttpClient) {}

  getLatestVersion(): Observable<Version> {
    this.logger.info('GETing latest Version');
    return this.httpClient.get<Version>('/versions/latest')
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing latest Version: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}
