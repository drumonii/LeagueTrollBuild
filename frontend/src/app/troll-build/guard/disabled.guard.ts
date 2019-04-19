import { CanLoad, Route, UrlSegment } from '@angular/router';

export class DisabledGuard implements CanLoad {

  canLoad(route: Route, segments: UrlSegment[]): boolean {
    return false;
  }

}
