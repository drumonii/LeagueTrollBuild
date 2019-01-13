import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { GlobalErrorService } from './global-error.service';
import { ActuatorResponse } from '@admin-model/actuator-response';

describe('GlobalErrorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [GlobalErrorService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getGlobalErrors', () => {

    const requestMatch: RequestMatch = { method: 'GET', url: '/admin/actuator/metrics/tomcat.global.error' };

    it('should get global errors', inject([GlobalErrorService, HttpTestingController],
      (service: GlobalErrorService, httpMock: HttpTestingController) => {
      const mockServletErrors = 1;
      const mockActuatorResponse: ActuatorResponse = {
        name: 'tomcat.global.error',
        measurements: [
          {
            statistic: 'COUNT',
            value: mockServletErrors
          }
        ],
      };

      service.getGlobalErrors().subscribe(servletErrors => {
        expect(servletErrors).toBe(mockServletErrors);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockActuatorResponse);
    }));

    it('should get global errors with REST error', inject([GlobalErrorService, HttpTestingController],
      (service: GlobalErrorService, httpMock: HttpTestingController) => {
      service.getGlobalErrors().subscribe(servletErrors => {
        expect(servletErrors).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);

      const errorEvent = document.createEvent('Event');
      errorEvent.initEvent('ErrorEvent', false, false);
      testReq.error(errorEvent as ErrorEvent);
    }));

  });

});
