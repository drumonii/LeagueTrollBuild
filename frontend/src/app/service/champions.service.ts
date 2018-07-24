import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Champion } from '@model/champion';

@Injectable()
export class ChampionsService {

  constructor(private httpClient: HttpClient) {}

  getChampions(): Observable<Champion[]> {
    return this.httpClient.get<Champion[]>('/api/champions')
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error(`Caught error while GETing Champions: ${JSON.stringify(error)}`);
          return of([]);
        })
      );
  }

  getChampionTags(): Observable<string[]> {
    return this.httpClient.get<string[]>('/api/champions/tags')
      .pipe(
        catchError(() => of([]))
      );
  }

}
