import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { CpuUsageService } from './cpu-usage.service';
import { ActuatorResponse } from '@admin-model/actuator-response';

describe('CpuUsageService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CpuUsageService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getCpuUsagePerc', () => {

    const requestMatch: RequestMatch = { method: 'GET', url: '/admin/actuator/metrics/system.cpu.usage' };

    it('should get cpu usage', inject([CpuUsageService, HttpTestingController],
      (service: CpuUsageService, httpMock: HttpTestingController) => {
      const mockCpuUsage = 0.13347316594947334;
      const mockActuatorResponse: ActuatorResponse = {
        name: 'system.cpu.usage',
        measurements: [
          {
            statistic: 'VALUE',
            value: mockCpuUsage
          }
        ],
      };

      service.getCpuUsagePerc().subscribe(cpuUsage => {
        expect(cpuUsage).toBe('13.347');
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockActuatorResponse);
    }));

    it('should get cpu usage with REST error', inject([CpuUsageService, HttpTestingController],
      (service: CpuUsageService, httpMock: HttpTestingController) => {
      service.getCpuUsagePerc().subscribe(cpuUsage => {
        expect(cpuUsage).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);

      const errorEvent = document.createEvent('Event');
      errorEvent.initEvent('ErrorEvent', false, false);
      testReq.error(errorEvent as ErrorEvent);
    }));

  });

  describe('getCpuCount', () => {

    const requestMatch: RequestMatch = { method: 'GET', url: '/admin/actuator/metrics/system.cpu.count' };

    it('should get cpu count', inject([CpuUsageService, HttpTestingController],
      (service: CpuUsageService, httpMock: HttpTestingController) => {
      const mockCpuCount = 8.0;
      const mockActuatorResponse: ActuatorResponse = {
        name: 'system.cpu.count',
        measurements: [
          {
            statistic: 'VALUE',
            value: mockCpuCount
          }
        ],
      };

      service.getCpuCount().subscribe(cpuCount => {
        expect(cpuCount).toBe(mockCpuCount);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockActuatorResponse);
    }));

    it('should get cpu count with REST error', inject([CpuUsageService, HttpTestingController],
      (service: CpuUsageService, httpMock: HttpTestingController) => {
      service.getCpuCount().subscribe(cpuCount => {
        expect(cpuCount).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);

      const errorEvent = document.createEvent('Event');
      errorEvent.initEvent('ErrorEvent', false, false);
      testReq.error(errorEvent as ErrorEvent);
    }));

  });

});
