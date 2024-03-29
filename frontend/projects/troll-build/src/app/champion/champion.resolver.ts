import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { Champion } from '@ltb-model/champion';
import { ChampionService } from './champion.service';

@Injectable()
export class ChampionResolver implements Resolve<Champion | null> {

  constructor(private championService: ChampionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Champion | null> {
    const name = route.paramMap.get('name');

    return this.championService.getChampion(name)
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
