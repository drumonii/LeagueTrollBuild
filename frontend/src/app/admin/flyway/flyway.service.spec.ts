import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { FlywayService } from './flyway.service';
import { FlywayResponse } from './flyway-response';
import { ADMIN_INTERCEPT_HEADER } from '@admin-interceptor/admin-http-interceptor-headers';

describe('FlywayService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [FlywayService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getFlyway', () => {

    const requestMatch: RequestMatch = { method: 'GET', url: '/actuator/flyway' };

    it('should get flyway migrations', inject([FlywayService, HttpTestingController],
      (service: FlywayService, httpMock: HttpTestingController) => {
      const mockFlywayResponse: FlywayResponse = {
        contexts: {
          application: {
            flywayBeans: {
              flyway: {
                migrations: [
                  {
                    type: 'SQL',
                    checksum: -352371787,
                    version: '1440638259',
                    description: 'CHAMPION',
                    script: 'V1440638259__CHAMPION.sql',
                    state: 'SUCCESS',
                    installedBy: 'postgres',
                    installedOn: '2018-08-21T02:18:28.917Z',
                    installedRank: 1,
                    executionTime: 8
                  }
                ]
              }
            }
          }
        }
      };

      service.getFlyway().subscribe(flywayMigrations => {
        expect(flywayMigrations).toEqual(mockFlywayResponse.contexts.application.flywayBeans.flyway.migrations);
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.has(ADMIN_INTERCEPT_HEADER)).toBe(true);

      testReq.flush(mockFlywayResponse);
    }));

    it('should get flyway migrations with REST error', inject([FlywayService, HttpTestingController],
      (service: FlywayService, httpMock: HttpTestingController) => {
      service.getFlyway().subscribe(flywayMigrations => {
        expect(flywayMigrations).toEqual([]);
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.has(ADMIN_INTERCEPT_HEADER)).toBe(true);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

});
