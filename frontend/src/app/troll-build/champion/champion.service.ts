import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Logger } from '@ltb-service/logger.service';
import { Champion } from '@ltb-model/champion';
import { TrollBuild } from '@ltb-model/troll-build';
import { GameMap } from '@ltb-model/game-map';
import { Build } from '@ltb-model/build';

@Injectable()
export class ChampionService {

  constructor(private logger: Logger, private httpClient: HttpClient) {}

  getChampion(name: string): Observable<Champion> {
    this.logger.info('GETing Champion', name);
    return this.httpClient.get<Champion>(`/champions/${name}`)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing Champion ${name}: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  getTrollBuild(name: string, gameMapId?: number): Observable<TrollBuild> {
    const params = gameMapId ? new HttpParams().set('mapId', gameMapId.toString()) : new HttpParams();
    this.logger.info(`GETing a Troll Build for Champion ${name} and map Id ${params.get('mapId')}`);
    const options = {
      params
    };
    return this.httpClient.get<TrollBuild>(`/champions/${name}/troll-build`, options)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing a Troll Build for Champion ${name} and params
            ${JSON.stringify(params)}: ${JSON.stringify(error)}`);
          return of(new TrollBuild());
        })
      );
  }

  mapsForTrollBuild(): Observable<GameMap[]> {
    this.logger.info('GETing Game Maps for Troll Build');
    return this.httpClient.get<GameMap[]>('/maps/for-troll-build')
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing Game Maps for Troll Build: ${JSON.stringify(error)}`);
          return of([]);
        })
      );
  }

  saveBuild(build: Build): Observable<HttpResponse<Build>> {
    this.logger.info('POSTing build', JSON.stringify(build));
    return this.httpClient.post<HttpResponse<Build>>('/builds', build,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        }),
        observe: 'response'
      })
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while POSTing build ${JSON.stringify(build)}: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}
