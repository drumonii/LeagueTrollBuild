import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';

import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, finalize, map } from 'rxjs/operators';

import { Logger } from '@service/logger.service';
import { AdminUserDetails } from '@security/admin-user-details';
import { AdminLoginResponse, AdminLoginStatus } from '@security/admin-login-response';
import { AdminLogoutResponse } from '@security/admin-logout-response';

@Injectable({
  providedIn: 'root'
})
export class AdminAuthService {

  static adminUserDetailsKey = 'adminUserDetails';

  private adminUserDetails$ = new BehaviorSubject<AdminUserDetails>(null);

  constructor(private logger: Logger, private httpClient: HttpClient) {}

  get adminUserDetails(): Observable<AdminUserDetails> {
    this.adminUserDetails$.next(JSON.parse(localStorage.getItem(AdminAuthService.adminUserDetailsKey)));
    return this.adminUserDetails$.asObservable();
  }

  loginAdmin(username: string, password: string): Observable<AdminLoginResponse> {
    const body = `username=${username}&password=${password}`;
    const headers = new HttpHeaders().set('content-type', 'application/x-www-form-urlencoded');
    const options = {
      headers: headers
    };
    return this.httpClient.post<AdminLoginResponse>('/admin/login', body, options)
      .pipe(
        map((loginResponse) => {
          if (loginResponse && loginResponse.status === AdminLoginStatus.SUCCESS) {
            this.addAdminUserDetails(loginResponse.userDetails);
          }
          return loginResponse;
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while POSTing /admin/login ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  logoutAdmin(): Observable<AdminLogoutResponse> {
    return this.httpClient.post<AdminLoginResponse>('/admin/logout', {})
      .pipe(
        finalize(() => {
          this.removeAdminUserDetails();
          this.adminRefresh().subscribe(); // trigger new csrf token for login page
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while POSTing /admin/logout ${JSON.stringify(error)}`);
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
    return this.httpClient.get<AdminUserDetails>('/admin/authentication')
      .pipe(
        map((adminUserDetails) => {
          this.addAdminUserDetails(adminUserDetails);
          return true;
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.debug(`Caught error while GETing /admin/authentication ${JSON.stringify(error)}`);
          this.removeAdminUserDetails();
          return of(false);
        })
      );
  }

  private adminRefresh(): Observable<any> {
    return this.httpClient.get<any>('/admin/refresh')
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.logger.debug(`Caught error while GETing admin/refresh ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}
