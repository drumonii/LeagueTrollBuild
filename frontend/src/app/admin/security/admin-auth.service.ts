import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, finalize, map } from 'rxjs/operators';

import { AdminLogger } from '@admin-service/admin-logger.service';
import { AdminUserDetails } from '@admin-security/admin-user-details';
import { AdminLoginResponse, AdminLoginStatus } from '@admin-security/admin-login-response';
import { AdminLogoutResponse } from '@admin-security/admin-logout-response';
import { AdminService } from '@admin-service/admin-service';

@Injectable({
  providedIn: 'root'
})
export class AdminAuthService extends AdminService {

  static adminUserDetailsKey = 'adminUserDetails';

  private adminUserDetails$ = new BehaviorSubject<AdminUserDetails>(null);

  constructor(private logger: AdminLogger, private httpClient: HttpClient) {
    super();
  }

  get adminUserDetails(): Observable<AdminUserDetails> {
    this.adminUserDetails$.next(JSON.parse(localStorage.getItem(AdminAuthService.adminUserDetailsKey)));
    return this.adminUserDetails$.asObservable();
  }

  loginAdmin(username: string, password: string): Observable<AdminLoginResponse> {
    const body = `username=${username}&password=${password}`;
    const headers = this.getBaseHttpHeaders()
      .set('content-type', 'application/x-www-form-urlencoded');
    const options = {
      headers
    };
    return this.httpClient.post<AdminLoginResponse>('/login', body, options)
      .pipe(
        map((loginResponse) => {
          if (loginResponse && loginResponse.status === AdminLoginStatus.SUCCESS) {
            this.addAdminUserDetails(loginResponse.userDetails);
          }
          return loginResponse;
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while POSTing admin login ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  logoutAdmin(): Observable<AdminLogoutResponse> {
    const headers = this.getBaseHttpHeaders();
    const options = {
      headers
    };
    return this.httpClient.post<AdminLoginResponse>('/logout', {}, options)
      .pipe(
        finalize(() => {
          this.removeAdminUserDetails();
          this.adminRefresh().subscribe(); // trigger new csrf token for login page
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while POSTing admin logout ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  private addAdminUserDetails(adminUserDetails: AdminUserDetails): void {
    localStorage.setItem(AdminAuthService.adminUserDetailsKey, JSON.stringify(adminUserDetails));
    this.adminUserDetails$.next(adminUserDetails);
  }

  private removeAdminUserDetails(): void {
    localStorage.removeItem(AdminAuthService.adminUserDetailsKey);
    this.adminUserDetails$.next(null);
  }

  isAuthenticated(): Observable<boolean> {
    const headers = this.getBaseHttpHeaders();
    const options = {
      headers
    };
    return this.httpClient.get<AdminUserDetails>('/authentication', options)
      .pipe(
        map((adminUserDetails) => {
          this.addAdminUserDetails(adminUserDetails);
          return true;
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.warn(`Caught error while GETing admin authentication ${JSON.stringify(error)}`);
          this.removeAdminUserDetails();
          return of(false);
        })
      );
  }

  private adminRefresh(): Observable<any> {
    const headers = this.getBaseHttpHeaders();
    const options = {
      headers
    };
    return this.httpClient.get<any>('/refresh', options)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.logger.warn(`Caught error while GETing admin refresh ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}
