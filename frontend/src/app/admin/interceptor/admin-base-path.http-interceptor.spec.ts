import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient, HttpHeaders } from '@angular/common/http';

import { AdminBasePathHttpInterceptor } from './admin-base-path.http-interceptor';
import { ADMIN_INTERCEPT_HEADER, ADMIN_INTERCEPT_HEADER_VAL } from '@admin-interceptor/admin-http-interceptor-headers';

describe('AdminBasePathHttpInterceptor', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{
        provide: HTTP_INTERCEPTORS,
        useClass: AdminBasePathHttpInterceptor,
        multi: true
      }]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('admin request', () => {

    it('should prepend the admin base path', inject([HttpClient, HttpTestingController],
      (http: HttpClient, httpMock: HttpTestingController) => {
      const headers = new HttpHeaders()
        .set(ADMIN_INTERCEPT_HEADER, ADMIN_INTERCEPT_HEADER_VAL);
      const options = {
        headers
      };
      http.get('/some-url', options).subscribe(res => {
        expect(res).toBeTruthy();
      });

      const requestMatch: RequestMatch = { method: 'GET', url: '/admin/some-url' };

      const testReq = httpMock.expectOne(requestMatch);
      testReq.flush({});
    }));

  });

  describe('troll build request', () => {

    it('should ignore the request', inject([HttpClient, HttpTestingController],
      (http: HttpClient, httpMock: HttpTestingController) => {
      http.get('/some-url').subscribe(res => {
        expect(res).toBeTruthy();
      });

      const requestMatch: RequestMatch = { method: 'GET', url: '/some-url' };

      const testReq = httpMock.expectOne(requestMatch);
      testReq.flush({});
    }));

  });

});
