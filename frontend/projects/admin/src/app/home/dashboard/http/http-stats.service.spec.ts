import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { HttpStatsService } from './http-stats.service';
import { ActuatorHttpResponse } from '../actuator-http-response';

describe('HttpStatsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [HttpStatsService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getHttpStats', () => {

    const requestMatch: RequestMatch = { method: 'GET', url: '/actuator/metrics/http.server.requests' };

    it('should get the HTTP server requests', inject([HttpStatsService, HttpTestingController],
      (service: HttpStatsService, httpMock: HttpTestingController) => {
      const mockHttpServerRequests: ActuatorHttpResponse = {
        measurements: [
          {
            statistic: 'COUNT',
            value: 79
          },
          {
            statistic: 'MAX',
            value: 0.0053186
          }
        ],
        availableTags: [
          {
            tag: 'exception',
            values: [
              'None',
              'BadRequestException'
            ]
          },
          {
            tag: 'method',
            values: [
              'POST',
              'GET'
            ]
          },
          {
            tag: 'uri',
            values: [
              '/**',
              'root',
              '/api/admin/actuator/metrics/{requiredMetricName}',
            ]
          },
          {
            tag: 'outcome',
            values: [
              'CLIENT_ERROR',
              'SUCCESS'
            ]
          },
          {
            tag: 'status',
            values: [
              '400',
              '200'
            ]
          }
        ]
      };

      service.getHttpStats().subscribe(httpStats => {
        expect(httpStats).toEqual({
          measurements: {
            count: 79,
            max: 0.0053186,
          },
          exceptions: [
            'BadRequestException'
          ],
          uris: [
            '/api/admin/actuator/metrics/{requiredMetricName}'
          ],
          statuses: [
            '400',
            '200'
          ]
        });
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockHttpServerRequests);
    }));

    it('should get the HTTP server requests', inject([HttpStatsService, HttpTestingController],
      (service: HttpStatsService, httpMock: HttpTestingController) => {
      service.getHttpStats().subscribe(httpStats => {
        expect(httpStats).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });
});
