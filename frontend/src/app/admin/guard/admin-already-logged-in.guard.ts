import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';

import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';

import { AdminAuthService } from '@security/admin-auth.service';

@Injectable()
export class AdminAlreadyLoggedInGuard implements CanActivate {

  constructor(private authService: AdminAuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
    return this.authService.isAuthenticated()
      .pipe(
        take(1),
        map((isAdminLoggedIn) => !isAdminLoggedIn || this.redirectToAdminHome())
      );
  }

  private redirectToAdminHome(): UrlTree {
    return this.router.createUrlTree(['/admin']);
  }

}
