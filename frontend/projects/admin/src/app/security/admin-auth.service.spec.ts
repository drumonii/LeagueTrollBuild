import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { AdminUserDetails } from './admin-user-details';
import { AdminAuthService } from './admin-auth.service';
import { AdminLoginResponse, AdminLoginStatus } from './admin-login-response';
import { AdminLogoutResponse, AdminLogoutStatus } from './admin-logout-response';

describe('AdminAuthService', () => {

  const mockAdminUserDetails: AdminUserDetails = {
    username: 'admin',
    authorities: [
      {
        authority: 'ROLE_ADMIN'
      }
    ]
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AdminAuthService]
    });
  });

  afterEach(() => {
    sessionStorage.clear();
  });

  describe('getAdminUserDetails', () => {

    it('with admin user details in sessionStorage', inject([AdminAuthService], (authService: AdminAuthService) => {
      sessionStorage.setItem(AdminAuthService.adminUserDetailsKey, JSON.stringify(mockAdminUserDetails));

      authService.adminUserDetails.subscribe(adminUserDetails => {
        expect(adminUserDetails).toEqual(mockAdminUserDetails);
      });
    }));

    it('without admin user details in sessionStorage', inject([AdminAuthService], (authService: AdminAuthService) => {
      authService.adminUserDetails.subscribe(adminUserDetails => {
        expect(adminUserDetails).toBeNull();
      });
    }));

  });

  describe('loginAdmin', () => {

    const requestMatch: RequestMatch = { method: 'POST', url: '/login' };

    it('should set adminUserDetails in sessionStorage with successful login',
      inject([AdminAuthService, HttpTestingController], (authService: AdminAuthService, httpMock: HttpTestingController) => {
      const mockSuccessfulLoginResponse: AdminLoginResponse = {
        status: AdminLoginStatus.SUCCESS,
        message: 'Login successful',
        userDetails: mockAdminUserDetails
      };

      authService.loginAdmin('some_username', 'some_password').subscribe(loginResponse => {
        expect(loginResponse).toEqual(mockSuccessfulLoginResponse);
        expect(sessionStorage.getItem(AdminAuthService.adminUserDetailsKey))
          .toEqual(JSON.stringify(mockSuccessfulLoginResponse.userDetails));
        authService.adminUserDetails.subscribe(adminUserDetails => {
          expect(adminUserDetails).toEqual(adminUserDetails);
        });
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.get('content-type')).toBe('application/x-www-form-urlencoded');
      expect(testReq.request.body).toEqual('username=some_username&password=some_password');

      testReq.flush(mockSuccessfulLoginResponse);
    }));

    it('should not set adminUserDetails in sessionStorage with failed login',
      inject([AdminAuthService, HttpTestingController], (authService: AdminAuthService, httpMock: HttpTestingController) => {
      const mockFailedLoginResponse: AdminLoginResponse = {
        status: AdminLoginStatus.FAILED,
        message: 'Bad Credentials',
      };

      authService.loginAdmin('some_username', 'some_password').subscribe(loginResponse => {
        expect(loginResponse).toEqual(mockFailedLoginResponse);
        expect(sessionStorage.getItem(AdminAuthService.adminUserDetailsKey)).toBeNull();
        authService.adminUserDetails.subscribe(adminUserDetails => {
          expect(adminUserDetails).toBeNull();
        });
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.get('content-type')).toBe('application/x-www-form-urlencoded');
      expect(testReq.request.body).toEqual('username=some_username&password=some_password');

      testReq.flush(mockFailedLoginResponse);
    }));

    it('should not set adminUserDetails in sessionStorage with REST error',
      inject([AdminAuthService, HttpTestingController], (authService: AdminAuthService, httpMock: HttpTestingController) => {
      authService.loginAdmin('some_username', 'some_password').subscribe(loginResponse => {
        expect(loginResponse).toBeNull();
        expect(sessionStorage.getItem(AdminAuthService.adminUserDetailsKey)).toBeNull();
        authService.adminUserDetails.subscribe(adminUserDetails => {
          expect(adminUserDetails).toBeNull();
        });
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.get('content-type')).toBe('application/x-www-form-urlencoded');
      expect(testReq.request.body).toEqual('username=some_username&password=some_password');

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('isAuthenticated', () => {

    const requestMatch: RequestMatch = { method: 'GET', url: '/authentication' };

    it('should return true if authenticated and set adminUserDetails in sessionStorage',
      inject([AdminAuthService, HttpTestingController], (authService: AdminAuthService, httpMock: HttpTestingController) => {
      authService.isAuthenticated().subscribe(isAuthenticated => {
        expect(isAuthenticated).toBe(true);
        expect(sessionStorage.getItem(AdminAuthService.adminUserDetailsKey)).toEqual(JSON.stringify(mockAdminUserDetails));
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockAdminUserDetails);
    }));

    it('should return false if not authenticated and remove adminUserDetails in sessionStorage',
      inject([AdminAuthService, HttpTestingController], (authService: AdminAuthService, httpMock: HttpTestingController) => {
      sessionStorage.setItem(AdminAuthService.adminUserDetailsKey, JSON.stringify(mockAdminUserDetails));

      authService.isAuthenticated().subscribe(isAuthenticated => {
        expect(isAuthenticated).toBe(false);
        expect(sessionStorage.getItem(AdminAuthService.adminUserDetailsKey)).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('logoutAdmin', () => {

    const logoutRequestMatch: RequestMatch = { method: 'POST', url: '/logout' };
    const refreshRequestMatch: RequestMatch = { method: 'GET', url: '/refresh' };

    beforeEach(() => {
      sessionStorage.setItem(AdminAuthService.adminUserDetailsKey, JSON.stringify(mockAdminUserDetails));
    });

    afterEach(inject([AdminAuthService], (authService: AdminAuthService) => {
      expect(sessionStorage.getItem(AdminAuthService.adminUserDetailsKey)).toBeNull();

      authService.adminUserDetails.subscribe(adminUserDetails => {
        expect(adminUserDetails).toBeNull();
      });
    }));

    it('should remove adminUserDetails in sessionStorage',
      inject([AdminAuthService, HttpTestingController], (authService: AdminAuthService, httpMock: HttpTestingController) => {
      const mockSuccessfulLogoutResponse: AdminLogoutResponse = {
        status: AdminLogoutStatus.SUCCESS,
        message: 'Logout successful',
        userDetails: mockAdminUserDetails
      };

      authService.logoutAdmin().subscribe(logoutResponse => {
        expect(logoutResponse).toEqual(mockSuccessfulLogoutResponse);
      });

      const logoutTestReq = httpMock.expectOne(logoutRequestMatch);
      expect(logoutTestReq.request.body).toEqual({});

      logoutTestReq.flush(mockSuccessfulLogoutResponse);

      const refreshTestReq = httpMock.expectOne(refreshRequestMatch);

      refreshTestReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

    it('should remove adminUserDetails in sessionStorage with REST error',
      inject([AdminAuthService, HttpTestingController], (authService: AdminAuthService, httpMock: HttpTestingController) => {
      authService.logoutAdmin().subscribe(logoutResponse => {
        expect(logoutResponse).toBeNull();
      });

      const logoutTestReq = httpMock.expectOne(logoutRequestMatch);
      expect(logoutTestReq.request.body).toEqual({});

      logoutTestReq.error(new ErrorEvent('An unexpected error occurred'));

      const refreshTestReq = httpMock.expectOne(refreshRequestMatch);
      refreshTestReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

});
