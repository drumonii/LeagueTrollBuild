import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { ServletErrorService } from './servlet-error.service';
import { ActuatorResponse } from '@admin-model/actuator-response';

describe('ServletErrorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ServletErrorService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getServletErrors', () => {

    const requestMatch: RequestMatch = { method: 'GET', url: '/admin/actuator/metrics/tomcat.servlet.error' };

    it('should get servlet errors', inject([ServletErrorService, HttpTestingController],
      (service: ServletErrorService, httpMock: HttpTestingController) => {
      const mockServletErrors = 1;
      const mockActuatorResponse: ActuatorResponse = {
        name: 'tomcat.servlet.error',
        measurements: [
          {
            statistic: 'COUNT',
            value: mockServletErrors
          }
        ],
      };

      service.getServletErrors().subscribe(servletErrors => {
        expect(servletErrors).toBe(mockServletErrors);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockActuatorResponse);
    }));

    it('should get servlet errors with REST error', inject([ServletErrorService, HttpTestingController],
      (service: ServletErrorService, httpMock: HttpTestingController) => {
      service.getServletErrors().subscribe(servletErrors => {
        expect(servletErrors).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

});
