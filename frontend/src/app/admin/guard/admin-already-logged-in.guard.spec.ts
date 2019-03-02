import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';

import { of } from 'rxjs';

import { AdminAuthService } from '@admin-security/admin-auth.service';
import { AdminAlreadyLoggedInGuard } from './admin-already-logged-in.guard';

describe('AdminAlreadyLoggedInGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [AdminAlreadyLoggedInGuard]
    });
  });

  beforeEach(inject([Router], (router: Router) => {
    spyOn(router, 'createUrlTree');
  }));

  it('should return false and redirect to /admin if admin is already logged in',
    inject([AdminAlreadyLoggedInGuard, AdminAuthService, Router],
      (guard: AdminAlreadyLoggedInGuard, authService: AdminAuthService, router: Router) => {
    spyOn(authService, 'isAuthenticated').and.returnValue(of(true));

    guard.canActivate(null, null).subscribe(canActivate => {
      expect(canActivate).toBe(router.createUrlTree(['/admin']));
      expect(router.createUrlTree).toHaveBeenCalledWith(['/admin']);
    });
  }));

  it('should return true if admin is not authenticated',
    inject([AdminAlreadyLoggedInGuard, AdminAuthService, Router],
      (guard: AdminAlreadyLoggedInGuard, authService: AdminAuthService, router: Router) => {
    spyOn(authService, 'isAuthenticated').and.returnValue(of(false));

    guard.canActivate(null, null).subscribe(canActivate => {
      expect(canActivate).toBe(true);
      expect(router.createUrlTree).not.toHaveBeenCalled();
    });
  }));

});
