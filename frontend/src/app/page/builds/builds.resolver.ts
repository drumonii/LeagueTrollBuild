import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';

import { Observable, EMPTY } from 'rxjs';
import { mergeMap, map } from 'rxjs/operators';

import { BuildsResolverData } from './builds.resolver.data';
import { BuildsService } from '@service/builds.service';

@Injectable()
export class BuildsResolver implements Resolve<BuildsResolverData> {

  constructor(private buildsService: BuildsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<BuildsResolverData> {
    const buildId = +route.paramMap.get('buildId');
    if (buildId) {
      return this.buildsService.getBuild(buildId)
      .pipe(
        map(build => {
          return { id: buildId, savedBuild: build };
        })
      );
    }
    return this.getRandomBuildId().pipe(
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
