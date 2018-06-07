import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { GameMap } from '@model/game-map';

@Injectable()
export class GameMapsService {

  constructor(private httpClient: HttpClient) {}

  forTrollBuild(): Observable<GameMap[]> {
    return this.httpClient.get<GameMap[]>('/api/maps/for-troll-build')
      .pipe(
        catchError((error) => {
          console.error(`Caught error while GETing Game Maps for Troll Build: ${JSON.stringify(error)}`);
          return of([]);
        })
      );
  }

}
