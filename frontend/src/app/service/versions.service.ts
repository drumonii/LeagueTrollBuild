import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { catchError } from 'rxjs/operators';

import { Version } from '@model/version';

@Injectable()
export class VersionsService {

  constructor(private httpClient: HttpClient) {}

  getLatestVersion(): Observable<Version> {
    return this.httpClient.get<Version>('/api/versions/latest')
      .pipe(
        catchError((error) => {
          console.error(`Caught error while GETing latest Version: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}
