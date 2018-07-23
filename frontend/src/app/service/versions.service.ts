import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Version } from '@model/version';

@Injectable()
export class VersionsService {

  constructor(private httpClient: HttpClient) {}

  getLatestVersion(): Observable<Version> {
    return this.httpClient.get<Version>('/api/versions/latest')
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error(`Caught error while GETing latest Version: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}
