import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Logger } from '@ltb-service/logger.service';
import { Champion } from '@ltb-model/champion';

@Injectable()
export class ChampionsService {

  constructor(private logger: Logger, private httpClient: HttpClient) {}

  getChampions(): Observable<Champion[]> {
    this.logger.info('GETing Champions');
    return this.httpClient.get<Champion[]>('/champions')
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing Champions: ${JSON.stringify(error)}`);
          return of([]);
        })
      );
  }

  getChampionTags(): Observable<string[]> {
    this.logger.info('GETing Champion tags');
    return this.httpClient.get<string[]>('/champions/tags')
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing Champion tags: ${JSON.stringify(error)}`);
          return of([]);
        })
      );
  }

}
