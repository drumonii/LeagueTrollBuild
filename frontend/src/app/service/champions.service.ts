import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { catchError } from 'rxjs/operators';

import { Champion } from '@model/champion';

@Injectable()
export class ChampionsService {

  constructor(private httpClient: HttpClient) {}

  getChampion(name: string): Observable<Champion> {
    return this.httpClient.get<Champion>(`/api/champions/${name}`)
      .pipe(
        catchError((error) => {
          console.error(`Caught error while GETing Champion ${name}: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  getChampions(): Observable<Champion[]> {
    return this.httpClient.get<Champion[]>('/api/champions')
      .pipe(
        catchError((error) => {
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
