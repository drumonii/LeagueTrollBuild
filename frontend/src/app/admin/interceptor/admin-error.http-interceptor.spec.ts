import { async, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

import { AdminErrorHttpInterceptor } from './admin-error.http-interceptor';

describe('AdminErrorHttpInterceptor', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [
        {
          provide: HTTP_INTERCEPTORS,
          useClass: AdminErrorHttpInterceptor,
          multi: true
        }
      ]
    });
  }));

  beforeEach(inject([Router], (router: Router) => {
    spyOn(router, 'navigate');
  }));

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('400 bad request', () => {

    afterEach(inject([Router], (router: Router) => {
      expect(router.navigate).not.toHaveBeenCalled();
    }));

    it('should throw the error', inject([HttpClient, HttpTestingController],
      (http: HttpClient, httpMock: HttpTestingController) => {
      http.get('/some-url')
        .pipe(
          catchError(() => of(null))
        )
        .subscribe(res => {
          expect(res).toBeNull();
        });

      const requestMatch: RequestMatch = { method: 'GET', url: '/some-url' };

      const testReq = httpMock.expectOne(requestMatch);
      testReq.flush({}, { status: 400, statusText: '400' });
    }));

  });

  describe('401 unauthorized', () => {

    afterEach(inject([Router], (router: Router) => {
      expect(router.navigate).toHaveBeenCalledWith(['/admin/login']);
    }));

    it('should redirect to admin login', inject([HttpClient, HttpTestingController],
      (http: HttpClient, httpMock: HttpTestingController) => {
      http.get('/some-url')
        .pipe(
          catchError(() => {
            fail('caught unexpected error');
            return of(null);
          })
        )
        .subscribe(res => {
          expect(res).toBeTruthy();
        });

      const requestMatch: RequestMatch = { method: 'GET', url: '/some-url' };

      const testReq = httpMock.expectOne(requestMatch);
      testReq.flush({}, { status: 401, statusText: '401' });
    }));

  });

  describe('403 forbidden', () => {

    afterEach(inject([Router], (router: Router) => {
      expect(router.navigate).toHaveBeenCalledWith(['/admin/login']);
    }));

    it('should redirect to admin login', inject([HttpClient, HttpTestingController],
      (http: HttpClient, httpMock: HttpTestingController) => {
      http.get('/some-url')
        .pipe(
          catchError(() => {
            fail('caught unexpected error');
            return of(null);
          })
        )
        .subscribe(res => {
          expect(res).toBeTruthy();
        });

      const requestMatch: RequestMatch = { method: 'GET', url: '/some-url' };

      const testReq = httpMock.expectOne(requestMatch);
      testReq.flush({}, { status: 403, statusText: '403' });
    }));

  });

  describe('500 internal server error', () => {

    afterEach(inject([Router], (router: Router) => {
      expect(router.navigate).not.toHaveBeenCalled();
    }));

    it('should throw the error', inject([HttpClient, HttpTestingController],
      (http: HttpClient, httpMock: HttpTestingController) => {
      http.get('/some-url')
        .pipe(
          catchError(() => of(null))
        )
        .subscribe(res => {
          expect(res).toBeNull();
        });

      const requestMatch: RequestMatch = { method: 'GET', url: '/some-url' };

      const testReq = httpMock.expectOne(requestMatch);
      testReq.flush({}, { status: 500, statusText: '500' });
    }));

  });

});
