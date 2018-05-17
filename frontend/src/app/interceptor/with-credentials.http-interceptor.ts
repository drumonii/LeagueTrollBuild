import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';

/**
 * `HttpInterceptor` which adds an `withCredentials` to all outgoing requests in order for XSRF to work properly with
 * cookies from a different domain.
 */
@Injectable()
export class WithCredentialsHttpInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req.clone({withCredentials: true}));
  }

}
