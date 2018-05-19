import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { catchError } from 'rxjs/operators';

import { Champion } from '@model/champion';
import { TrollBuild } from '@model/troll-build';

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

  getTrollBuild(name: string, gameMapId?: number): Observable<TrollBuild> {
    const params = gameMapId ? new HttpParams().set('mapId', gameMapId.toString()) : new HttpParams();
    return this.httpClient.get<TrollBuild>(`/api/champions/${name}/troll-build`, { params: params })
      .pipe(
        catchError((error) => {
          console.error(`Caught error while GETing a Troll Build for Champion ${name} and params ${JSON.stringify(params)}: ${JSON.stringify(error)}`);
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
