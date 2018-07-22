import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { Champion } from '@model/champion';
import { ChampionsService } from '@service/champions.service';

@Injectable()
export class ChampionResolver implements Resolve<Champion> {

  constructor(private championsService: ChampionsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Champion> {
    const name = route.paramMap.get('name');

    return this.championsService.getChampion(name)
      .pipe(
        map(champion => {
          if (champion) {
            return champion;
          } else {
            this.router.navigate(['/champions']);
            return null;
          }
        })
      );
  }

}
