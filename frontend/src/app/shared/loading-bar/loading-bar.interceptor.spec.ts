import { inject, TestBed } from '@angular/core/testing';
import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { LoadingBarService } from './loading-bar.service';
import { LoadingBarModule } from '@loading-bar/loading-bar.module';

describe('LoadingBarInterceptor', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LoadingBarModule, HttpClientTestingModule],
    });
  });

  beforeEach(inject([LoadingBarService], (service: LoadingBarService) => {
    spyOn(service, 'start');
    spyOn(service, 'complete');
  }));

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  it('should start and end progress on http request start and end', inject([HttpClient, HttpTestingController, LoadingBarService],
    (http: HttpClient, httpMock: HttpTestingController, service: LoadingBarService) => {
    http.get('/test').subscribe(() => {
      expect(service.start).toHaveBeenCalled();
    });

    const testReq = httpMock.expectOne('/test');

    testReq.flush({});

    expect(service.complete).toHaveBeenCalled();
  }));

});
