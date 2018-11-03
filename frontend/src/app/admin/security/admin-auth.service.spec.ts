import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { AdminUserDetails } from '@security/admin-user-details';
import { AdminLoginResponse, AdminLoginStatus } from '@security/admin-login-response';
import { AdminAuthService } from './admin-auth.service';

describe('AdminAuthService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AdminAuthService]
    });
  });

  afterEach(() => {
    localStorage.clear();
  });

  describe('getAdminUserDetails', () => {

    it('with admin user details in localStorage', inject([AdminAuthService], (authService: AdminAuthService) => {
      const mockAdminUserDetails: AdminUserDetails = {
        username: 'admin',
        authorities: [
          {
            authority: 'ROLE_ADMIN'
          }
        ]
      };

      localStorage.setItem(AdminAuthService.adminUserDetailsKey, JSON.stringify(mockAdminUserDetails));

      authService.adminUserDetails.subscribe(adminUserDetails => {
        expect(adminUserDetails).toEqual(mockAdminUserDetails);
      });
    }));

    it('without admin user details in localStorage', inject([AdminAuthService], (authService: AdminAuthService) => {
      authService.adminUserDetails.subscribe(adminUserDetails => {
        expect(adminUserDetails).toBeNull();
      });
    }));

  });

  describe('loginAdmin', () => {

    const requestMatch: RequestMatch = { method: 'POST', url: '/admin/login' };

    it('should set adminUserDetails in localStorage with successful login',
      inject([AdminAuthService, HttpTestingController], (authService: AdminAuthService, httpMock: HttpTestingController) => {
      const mockSuccessfulLoginResponse: AdminLoginResponse = {
        status: AdminLoginStatus.SUCCESS,
        message: 'Login successful',
        userDetails: {
          username: 'admin',
          authorities: [
            {
              authority: 'ROLE_ADMIN'
            }
          ]
        }
      };

      authService.loginAdmin('some_username', 'some_password').subscribe(loginResponse => {
        expect(loginResponse).toEqual(mockSuccessfulLoginResponse);
        expect(localStorage.getItem(AdminAuthService.adminUserDetailsKey)).toEqual(JSON.stringify(mockSuccessfulLoginResponse.userDetails));
        authService.adminUserDetails.subscribe(adminUserDetails => {
          expect(adminUserDetails).toEqual(adminUserDetails);
        });
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.get('content-type')).toBe('application/x-www-form-urlencoded');
      expect(testReq.request.body).toEqual('username=some_username&password=some_password');

      testReq.flush(mockSuccessfulLoginResponse);
    }));

    it('should not set adminUserDetails in localStorage with failed login',
      inject([AdminAuthService, HttpTestingController], (authService: AdminAuthService, httpMock: HttpTestingController) => {
      const mockFailedLoginResponse: AdminLoginResponse = {
        status: AdminLoginStatus.FAILED,
        message: 'Bad Credentials',
      };

      authService.loginAdmin('some_username', 'some_password').subscribe(loginResponse => {
        expect(loginResponse).toEqual(mockFailedLoginResponse);
        expect(localStorage.getItem(AdminAuthService.adminUserDetailsKey)).toBeNull();
        authService.adminUserDetails.subscribe(adminUserDetails => {
          expect(adminUserDetails).toBeNull();
        });
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.get('content-type')).toBe('application/x-www-form-urlencoded');
      expect(testReq.request.body).toEqual('username=some_username&password=some_password');

      testReq.flush(mockFailedLoginResponse);
    }));

    it('should not set adminUserDetails in localStorage with REST error',
      inject([AdminAuthService, HttpTestingController], (authService: AdminAuthService, httpMock: HttpTestingController) => {
      authService.loginAdmin('some_username', 'some_password').subscribe(loginResponse => {
        expect(loginResponse).toBeNull();
        expect(localStorage.getItem(AdminAuthService.adminUserDetailsKey)).toBeNull();
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

});
