import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';

import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';

import { AdminAuthService } from '@security/admin-auth.service';

@Injectable()
export class AdminAlreadyLoggedInGuard implements CanActivate {

  constructor(private authService: AdminAuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.authService.isAuthenticated()
      .pipe(
        take(1),
        map((isAdminLoggedIn) => {
          if (isAdminLoggedIn) {
            this.redirectToAdminHome();
            return false;
          }
          return true;
        })
      );
  }

  private redirectToAdminHome() {
    this.router.navigate(['/admin']);
  }

}
