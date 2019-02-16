import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';

import { of } from 'rxjs';

import { AdminAuthService } from '@security/admin-auth.service';
import { AdminGuard } from './admin.guard';

describe('AdminGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [AdminGuard]
    });
  });

  beforeEach(inject([Router], (router: Router) => {
    spyOn(router, 'createUrlTree');
  }));

  it('should return false and redirect to / if admin is not authenticated',
    inject([AdminGuard, AdminAuthService, Router], (guard: AdminGuard, authService: AdminAuthService, router: Router) => {
    spyOn(authService, 'isAuthenticated').and.returnValue(of(false));

    guard.canActivate(null, null).subscribe(canActivate => {
      expect(canActivate).toBe(router.createUrlTree(['/']));
      expect(router.createUrlTree).toHaveBeenCalledWith(['/']);
    });
  }));

  it('should return true if admin is authenticated',
    inject([AdminGuard, AdminAuthService, Router], (guard: AdminGuard, authService: AdminAuthService, router: Router) => {
    spyOn(authService, 'isAuthenticated').and.returnValue(of(true));

    guard.canActivate(null, null).subscribe(canActivate => {
      expect(canActivate).toBe(true);
      expect(router.createUrlTree).not.toHaveBeenCalled();
    });
  }));

});
