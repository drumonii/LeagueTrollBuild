import { Injectable } from '@angular/core';
import { CanLoad, Route, UrlSegment } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class DisabledGuard implements CanLoad {

  canLoad(route: Route, segments: UrlSegment[]): boolean {
    return false;
  }

}
