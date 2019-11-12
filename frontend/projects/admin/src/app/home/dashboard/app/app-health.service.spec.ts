import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { of } from 'rxjs';

import { AppHealthService } from './app-health.service';
import { Paginated } from '../../../batch/datagrid';
import { BatchJobInstance } from '../../../batch/batch-job';
import { ScheduledTasks } from '../actuator-scheduled-tasks-response';
import { MetricsResponse } from '../actuator-metric-response';

describe('AppHealthService', () => {
  const getFailedJobsRequestMatch: RequestMatch = {
    method: 'GET',
    url: '/job-instances?jobExecution.status=FAILED&sort=jobExecution.startTime,desc'
  };

  const getNextScheduledAllRetrievalsJobRequestMatch: RequestMatch = {
    method: 'GET',
    url: '/actuator/scheduledtasks'
  };

  const getUptimeRequestMatch: RequestMatch = {
    method: 'GET',
    url: '/actuator/metrics/process.uptime'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AppHealthService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getAppHealth', () => {

    it('should get the app health', inject([AppHealthService], (service: AppHealthService) => {
      spyOn(service, 'getFailedJobs').and.returnValue(of({
        count: 0
      }));
      spyOn(service, 'getNextScheduledAllRetrievalsJob').and.returnValue(of(new Date()));
      spyOn(service, 'getUptime').and.returnValue(of({
        total: '',
        seconds: 0
      }));

      service.getAppHealth().subscribe(appHealth => {
        expect(appHealth).not.toEqual({
          failedJobs: null,
          nextScheduledAllRetrievalsJob: null,
          uptime: null
        });
      });
    }));

    it('should get the app health with REST error', inject([AppHealthService], (service: AppHealthService) => {
      spyOn(service, 'getFailedJobs').and.returnValue(of(null));
      spyOn(service, 'getNextScheduledAllRetrievalsJob').and.returnValue(of(null));
      spyOn(service, 'getUptime').and.returnValue(of(null));

      service.getAppHealth().subscribe(appHealth => {
        expect(appHealth).toEqual({
          failedJobs: null,
          nextScheduledAllRetrievalsJob: null,
          uptime: null
        });
      });
    }));

  });

  describe('getFailedJobs', () => {

    function getIsoDateString(now: Date) {
      const years = now.getFullYear();
      const month = zeroPad(now.getMonth() + 1);
      const day = zeroPad(now.getDate());

      return `${years}-${month}-${day}T04:00:00.000`;
    }

    function zeroPad(num: number) {
      if (num < 10) {
        return `0${num}`;
      }
      return num;
    }

    it('should get failed jobs', inject([AppHealthService, HttpTestingController],
      (service: AppHealthService, httpMock: HttpTestingController) => {
      const now = new Date();
      const nowString = getIsoDateString(now);

      const longAgo = new Date();
      longAgo.setDate(longAgo.getDate() - 30);
      const longAgoString = getIsoDateString(longAgo);

      const mockFailedJobInstances: Paginated<BatchJobInstance> = {
        content: [
          {
            id: 2,
            version: 0,
            name: 'summonerSpellsRetrievalJob',
            key: 'ed297beec86e42bf5c2988a3d9389a8f',
            jobExecution: {
              id: 2,
              version: 1,
              createTime: nowString,
              startTime: nowString,
              endTime: nowString,
              status: 'FAILED',
              exitCode: 'FAILED',
              exitMessage: '',
              lastUpdated: nowString
            }
          },
          {
            id: 1,
            version: 0,
            name: 'championsRetrievalJob',
            key: 'bb1756030fe33e9a798cca1b8e09d60e',
            jobExecution: {
              id: 1,
              version: 1,
              createTime: longAgoString,
              startTime: longAgoString,
              endTime: longAgoString,
              status: 'FAILED',
              exitCode: 'FAILED',
              exitMessage: '',
              lastUpdated: longAgoString
            }
          }
        ],
        pageable: {
          sort: {
            sorted: true,
            unsorted: false,
            empty: false
          },
          offset: 20,
          pageNumber: 0,
          pageSize: 20,
          unpaged: false,
          paged: true
        },
        totalElements: 2,
        totalPages: 1,
        last: true,
        number: 0,
        size: 20,
        sort: {
          sorted: true,
          unsorted: false,
          empty: false
        },
        numberOfElements: 2,
        first: true,
        empty: false
      };
      service.getFailedJobs().subscribe(failedJobs => {
        expect(failedJobs).toEqual({
          count: 1
        });
      });

      const testReq = httpMock.expectOne(getFailedJobsRequestMatch);

      testReq.flush(mockFailedJobInstances);
    }));

    it('should get failed jobs with REST error', inject([AppHealthService, HttpTestingController],
    (service: AppHealthService, httpMock: HttpTestingController) => {
      service.getFailedJobs().subscribe(failedJobs => {
        expect(failedJobs).toBeNull();
      });

      const testReq = httpMock.expectOne(getFailedJobsRequestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('getNextScheduledAllRetrievalsJob', () => {

    it('should get next scheduled all retrievals job', inject([AppHealthService, HttpTestingController],
      (service: AppHealthService, httpMock: HttpTestingController) => {
      const mockScheduledTasks: ScheduledTasks = {
        cron: [
          {
            runnable: {
              target: 'com.drumonii.loltrollbuild.batch.scheduling.RetrievalJobsScheduling.runAllRetrievalsJob'
            },
            expression: '0 0 4 * * ?'
          }
        ]
      };

      service.getNextScheduledAllRetrievalsJob().subscribe(nextScheduledAllRetrievalsJob => {
        expect(nextScheduledAllRetrievalsJob).not.toBeNull();
      });

      const testReq = httpMock.expectOne(getNextScheduledAllRetrievalsJobRequestMatch);

      testReq.flush(mockScheduledTasks);
    }));

    it('should get next scheduled all retrievals job with REST error', inject([AppHealthService, HttpTestingController],
      (service: AppHealthService, httpMock: HttpTestingController) => {
      service.getNextScheduledAllRetrievalsJob().subscribe(nextScheduledAllRetrievalsJob => {
        expect(nextScheduledAllRetrievalsJob).toBeNull();
      });

      const testReq = httpMock.expectOne(getNextScheduledAllRetrievalsJobRequestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('getUptime', () => {

    const uptimesTests = [
      {
        value: 120.246,
        seconds: 120,
        total: '2 minutes'
      },
      {
        value: 3700.146,
        seconds: 3700,
        total: '1 hour, 1 minute'
      },
      {
        value: 106491.959,
        seconds: 106491,
        total: '1 day, 5 hours, 34 minutes'
      },
      {
        value: 1064910.351,
        seconds: 1064910,
        total: '12 days, 7 hours, 48 minutes'
      }
    ];

    uptimesTests.forEach((timeTest) => {

      it(`should get the up time from seconds ${timeTest.seconds}`, inject([AppHealthService, HttpTestingController],
        (service: AppHealthService, httpMock: HttpTestingController) => {
        const mockUptimeMetricsResponse: MetricsResponse = {
          name: 'process.uptime',
          measurements: [
            {
              statistic: 'VALUE',
              value: timeTest.value
            }
          ]
        };

        service.getUptime().subscribe(uptime => {
          expect(uptime).toEqual({
            total: timeTest.total,
            seconds: timeTest.seconds
          });
        });

        const testReq = httpMock.expectOne(getUptimeRequestMatch);

        testReq.flush(mockUptimeMetricsResponse);
      }));

    });

    it('should get the up time REST error', inject([AppHealthService, HttpTestingController],
      (service: AppHealthService, httpMock: HttpTestingController) => {
      service.getUptime().subscribe(uptime => {
        expect(uptime).toBeNull();
      });

      const testReq = httpMock.expectOne(getUptimeRequestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });
});
