import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';

import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';

import { AdminAuthService } from '@admin-security/admin-auth.service';

@Injectable()
export class AdminGuard implements CanActivate {

  constructor(private authService: AdminAuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
    return this.authService.isAuthenticated()
      .pipe(
        take(1),
        map((isAdminLoggedIn) => isAdminLoggedIn || this.redirectToHome())
      );
  }

  private redirectToHome(): UrlTree {
    return this.router.createUrlTree(['/']);
  }

}
