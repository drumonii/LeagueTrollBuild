import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';

import { Observable, EMPTY } from 'rxjs';
import { mergeMap, map } from 'rxjs/operators';

import { SavedBuildsResolverData } from './saved-builds.resolver.data';
import { SavedBuildsService } from './saved-builds.service';

@Injectable()
export class SavedBuildsResolver implements Resolve<SavedBuildsResolverData> {

  constructor(private buildsService: SavedBuildsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<SavedBuildsResolverData> {
    const buildId = +route.paramMap.get('buildId');
    if (buildId) {
      return this.buildsService.getBuild(buildId)
        .pipe(
          map(build => {
            return { id: buildId, savedBuild: build };
          })
        );
    }
    return this.getRandomBuildId()
      .pipe(
        mergeMap(randomBuildId => {
          this.router.navigate(['/builds', randomBuildId]);
          return EMPTY;
        })
      );
  }

  private getRandomBuildId(): Observable<number> {
    const start = 1;
    return this.buildsService.countBuilds()
      .pipe(
        map(end => start === end ? start : start + Math.floor(Math.random() * (end - start)))
      );
  }

}
