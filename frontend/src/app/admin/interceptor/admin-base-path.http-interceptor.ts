import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { ADMIN_INTERCEPT_HEADER } from '@admin-interceptor/admin-http-interceptor-headers';

@Injectable()
export class AdminBasePathHttpInterceptor implements HttpInterceptor {

  private basePath = '/admin';

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.headers.has(ADMIN_INTERCEPT_HEADER)) {
      const headers = req.headers.delete(ADMIN_INTERCEPT_HEADER);
      return next.handle(req.clone({ url: this.basePath + req.url, headers }));
    }
    return next.handle(req);
  }

}
