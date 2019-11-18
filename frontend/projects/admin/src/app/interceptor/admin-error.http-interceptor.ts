import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Router } from '@angular/router';

import { EMPTY, Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class AdminErrorHttpInterceptor implements HttpInterceptor {

  private errorCodes = [401, 403];

  constructor(private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          if (this.errorCodes.includes(error.status)) {
            this.router.navigate(['/admin/login']);
            return EMPTY;
          }
          return throwError(error);
        })
      );
  }

}
