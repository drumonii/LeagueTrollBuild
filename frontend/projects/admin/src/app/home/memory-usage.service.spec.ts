import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { MemoryUsageService } from './memory-usage.service';
import { ActuatorResponse } from '@admin-model/actuator-response';

describe('MemoryUsageService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [MemoryUsageService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getMemoryUsage', () => {

    const requestMatch: RequestMatch = { method: 'GET', url: '/actuator/metrics/jvm.memory.used' };

    it('should get memory usage', inject([MemoryUsageService, HttpTestingController],
      (service: MemoryUsageService, httpMock: HttpTestingController) => {
      const mockMemoryUsage = 335306576; // bytes
      const mockActuatorResponse: ActuatorResponse = {
        name: 'jvm.memory.used',
        measurements: [
          {
            statistic: 'VALUE',
            value: mockMemoryUsage
          }
        ],
      };

      service.getMemoryUsage().subscribe(memoryUsage => {
        expect(memoryUsage).toBe('335.31');
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockActuatorResponse);
    }));

    it('should get memory usage with REST error', inject([MemoryUsageService, HttpTestingController],
      (service: MemoryUsageService, httpMock: HttpTestingController) => {
      service.getMemoryUsage().subscribe(memoryUsage => {
        expect(memoryUsage).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

});
