import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { of } from 'rxjs';

import { ResourcesService } from './resources.service';
import { MetricsResponse } from '../actuator-metric-response';
import { HealthResponse } from '../actuator-health-response';

describe('ResourcesService', () => {
  const cpuUsagePercRequestMatch: RequestMatch = {
    method: 'GET',
    url: '/actuator/metrics/system.cpu.usage'
  };

  const cpuCountRequestMatch: RequestMatch = {
    method: 'GET',
    url: '/actuator/metrics/system.cpu.count'
  };

  const memoryUsageRequestMatch: RequestMatch = {
    method: 'GET',
    url: '/actuator/metrics/jvm.memory.used'
  };

  const memoryMaxRequestMatch: RequestMatch = {
    method: 'GET',
    url: '/actuator/metrics/jvm.memory.max'
  };

  const diskUsageRequestMatch: RequestMatch = {
    method: 'GET',
    url: '/actuator/health'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ResourcesService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getResourceConsumption', () => {

    it('should get resource consumption', inject([ResourcesService], (service: ResourcesService) => {
      spyOn(service, 'getCpuUsage').and.returnValue(of(0));
      spyOn(service, 'getCpuCount').and.returnValue(of(0));
      spyOn(service, 'getMemoryUsage').and.returnValue(of(0));
      spyOn(service, 'getMemoryMax').and.returnValue(of(0));
      spyOn(service, 'getDiskUsage').and.returnValue(of({
        used: 0,
        max: 0
      }));

      service.getResourceConsumption().subscribe(resConsumption => {
        expect(resConsumption).not.toEqual({
          cpuUsage: null,
          memoryUsage: null,
          diskUsage: null
        });
      });
    }));

    it('should get resource consumption with REST error', inject([ResourcesService], (service: ResourcesService) => {
      spyOn(service, 'getCpuUsage').and.returnValue(of(null));
      spyOn(service, 'getCpuCount').and.returnValue(of(null));
      spyOn(service, 'getMemoryUsage').and.returnValue(of(null));
      spyOn(service, 'getMemoryMax').and.returnValue(of(null));
      spyOn(service, 'getDiskUsage').and.returnValue(of(null));

      service.getResourceConsumption().subscribe(resConsumption => {
        expect(resConsumption).toEqual({
          cpuUsage: null,
          memoryUsage: null,
          diskUsage: null
        });
      });
    }));

  });

  describe('getCpuUsage', () => {

    it('should get cpu usage', inject([ResourcesService, HttpTestingController],
      (service: ResourcesService, httpMock: HttpTestingController) => {
      const mockCpuUsage: MetricsResponse = {
        name: 'system.cpu.usage',
        measurements: [
          {
            statistic: 'VALUE',
            value: 0.13347316594947334
          }
        ],
      };

      service.getCpuUsage().subscribe(cpuUsage => {
        expect(cpuUsage).toBe(0.13347316594947334);
      });

      const testReq = httpMock.expectOne(cpuUsagePercRequestMatch);

      testReq.flush(mockCpuUsage);
    }));

    it('should get cpu usage with REST error', inject([ResourcesService, HttpTestingController],
      (service: ResourcesService, httpMock: HttpTestingController) => {
      service.getCpuUsage().subscribe(cpuUsage => {
        expect(cpuUsage).toBeNull();
      });

      const testReq = httpMock.expectOne(cpuUsagePercRequestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('getCpuCount', () => {

    it('should get cpu count', inject([ResourcesService, HttpTestingController],
      (service: ResourcesService, httpMock: HttpTestingController) => {
      const mockCpuCount: MetricsResponse = {
        name: 'system.cpu.count',
        measurements: [
          {
            statistic: 'VALUE',
            value: 8.0
          }
        ],
      };

      service.getCpuCount().subscribe(cpuCount => {
        expect(cpuCount).toBe(8.0);
      });

      const testReq = httpMock.expectOne(cpuCountRequestMatch);

      testReq.flush(mockCpuCount);
    }));

    it('should get cpu count with REST error', inject([ResourcesService, HttpTestingController],
      (service: ResourcesService, httpMock: HttpTestingController) => {
      service.getCpuCount().subscribe(cpuCount => {
        expect(cpuCount).toBeNull();
      });

      const testReq = httpMock.expectOne(cpuCountRequestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('getMemoryUsage', () => {

    it('should get memory usage', inject([ResourcesService, HttpTestingController],
      (service: ResourcesService, httpMock: HttpTestingController) => {
      const mockMemoryUsage: MetricsResponse = {
        name: 'jvm.memory.used',
        measurements: [
          {
            statistic: 'VALUE',
            value: 335306576
          }
        ],
      };

      service.getMemoryUsage().subscribe(memoryUsage => {
        expect(memoryUsage).toBe(335306576);
      });

      const testReq = httpMock.expectOne(memoryUsageRequestMatch);

      testReq.flush(mockMemoryUsage);
    }));

    it('should get memory usage with REST error', inject([ResourcesService, HttpTestingController],
      (service: ResourcesService, httpMock: HttpTestingController) => {
      service.getMemoryUsage().subscribe(memoryUsage => {
        expect(memoryUsage).toBeNull();
      });

      const testReq = httpMock.expectOne(memoryUsageRequestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('getMemoryMax', () => {

    it('should get memory max', inject([ResourcesService, HttpTestingController],
      (service: ResourcesService, httpMock: HttpTestingController) => {
      const mockMemoryMax: MetricsResponse = {
        name: 'jvm.memory.max',
        measurements: [
          {
            statistic: 'VALUE',
            value: 9900654589
          }
        ],
      };

      service.getMemoryMax().subscribe(memoryMax => {
        expect(memoryMax).toBe(9900654589);
      });

      const testReq = httpMock.expectOne(memoryMaxRequestMatch);

      testReq.flush(mockMemoryMax);
    }));

    it('should get memory max with REST error', inject([ResourcesService, HttpTestingController],
      (service: ResourcesService, httpMock: HttpTestingController) => {
      service.getMemoryMax().subscribe(memoryMax => {
        expect(memoryMax).toBeNull();
      });

      const testReq = httpMock.expectOne(memoryMaxRequestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('getDiskUsage', () => {

    it('should get disk usage', inject([ResourcesService, HttpTestingController],
      (service: ResourcesService, httpMock: HttpTestingController) => {
      const mockDiskUsage: HealthResponse = {
        components: {
          diskSpace: {
            details: {
              total: 510969704448,
              free: 258733219840
            }
          },
        }
      };

      service.getDiskUsage().subscribe(diskUsage => {
        expect(diskUsage).toEqual({
          used: (510969704448 - 258733219840),
          max: 510969704448
        });
      });

      const testReq = httpMock.expectOne(diskUsageRequestMatch);

      testReq.flush(mockDiskUsage);
    }));

    it('should get disk usage with REST error', inject([ResourcesService, HttpTestingController],
      (service: ResourcesService, httpMock: HttpTestingController) => {
      service.getDiskUsage().subscribe(diskUsage => {
        expect(diskUsage).toBeNull();
      });

      const testReq = httpMock.expectOne(diskUsageRequestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });
});
