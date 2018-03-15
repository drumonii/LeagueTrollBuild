import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';

/**
 * `HttpInterceptor` which prepends the base url to all outgoing requests.
 */
@Injectable()
export class BaseUrlHttpInterceptor implements HttpInterceptor {

  private baseUrl = 'http://localhost:8080';

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req.clone({url: this.baseUrl + req.url}));
  }

}
