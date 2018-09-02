import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { LoadingBarService } from './loading-bar.service';

@Injectable()
export class LoadingBarInterceptor implements HttpInterceptor {

  constructor(private loadingBar: LoadingBarService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.loadingBar.start();

    return next.handle(req)
      .pipe(
        finalize(() => this.loadingBar.complete())
      );
  }
}
