import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { WithCredentialsHttpInterceptor } from './with-credentials.http-interceptor';

describe('BaseUrlHttpInterceptor', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{
        provide: HTTP_INTERCEPTORS,
        useClass: WithCredentialsHttpInterceptor,
        multi: true
      }]
    });
  });

  it('should set withCredentials to true', inject([HttpClient, HttpTestingController],
    (http: HttpClient, httpMock: HttpTestingController) => {
    http.get('/some-url').subscribe(res => {
      expect(res).toEqual({});
    });

    const testReq = httpMock.expectOne('/some-url');
    expect(testReq.request.withCredentials).toBe(true);

    testReq.flush({});

    httpMock.verify();
  }));
});
