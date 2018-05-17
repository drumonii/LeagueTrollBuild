import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { catchError } from 'rxjs/operators';

import { Build } from '@model/build';

@Injectable()
export class BuildsService {

  constructor(private httpClient: HttpClient) {}

  saveBuild(build: Build): Observable<HttpResponse<Build>> {
    return this.httpClient.post<HttpResponse<Build>>('/api/builds', build,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        }),
        observe: 'response'
      })
    .pipe(
      catchError((error) => {
        console.error(`Caught error while POSTing build ${JSON.stringify(build)}: ${JSON.stringify(error)}`);
        return of(null);
      })
    );
  }

}
