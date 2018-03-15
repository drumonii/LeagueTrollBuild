import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { BaseUrlHttpInterceptor } from './base-url.http-interceptor';

describe('BaseUrlHttpInterceptor', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{
        provide: HTTP_INTERCEPTORS,
        useClass: BaseUrlHttpInterceptor,
        multi: true
      }]
    });
  });

  it('should prepend the base URL', inject([HttpClient, HttpTestingController],
    (http: HttpClient, httpMock: HttpTestingController) => {
    http.get('/some-url').subscribe(res => {
      expect(res).toEqual({});
    });

    const testReq = httpMock.expectOne('http://localhost:8080/some-url');
    testReq.flush({});

    httpMock.verify();
  }));
});
