import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';

import { AdminBasePathHttpInterceptor } from './admin-base-path.http-interceptor';

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

  it('should prepend the admin base path', inject([HttpClient, HttpTestingController],
    (http: HttpClient, httpMock: HttpTestingController) => {
    http.get('/some-url').subscribe(res => {
      expect(res).toBeTruthy();
    });

    const requestMatch: RequestMatch = { method: 'GET', url: '/admin/some-url' };

    const testReq = httpMock.expectOne(requestMatch);
    testReq.flush({});
  }));

});
