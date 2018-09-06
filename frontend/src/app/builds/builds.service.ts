import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Build } from '@model/build';

@Injectable()
export class BuildsService {

  constructor(private httpClient: HttpClient) {}

  getBuild(buildId: number): Observable<Build> {
    return this.httpClient.get<Build>(`/api/builds/${buildId}`)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error(`${JSON.stringify(error)}`);
          if (error.status === 404) {
            return of(null);
          }
          return of(new Build());
        })
      );
  }

  countBuilds(): Observable<number> {
    return this.httpClient.get<number>('/api/builds/count')
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error(`Caught error while GETing count of builds ${JSON.stringify(error)}`);
          return of(0);
        })
     );
  }

}
