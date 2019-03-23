import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { AdminBatchService } from './admin-batch.service';
import { PageRequest } from '@admin-model/page-request';
import { Paginated } from '@admin-model/paginated';
import { BatchJobInstance } from '@admin-model/batch-job-instance';
import { BatchStepExecution } from '@admin-model/batch-step-execution';
import { BatchJobExecution } from '@admin-model/batch-job-execution';
import { ADMIN_INTERCEPT_HEADER } from '@admin-interceptor/admin-http-interceptor-headers';

describe('AdminBatchService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AdminBatchService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getBatchJobInstances', () => {

    const mockPaginatedJobInstances: Paginated<BatchJobInstance> = {
      content: [
        {
          id: 2,
          version: 0,
          name: 'versionsRetrievalJob',
          key: '72fc7f3a90c487f9a3f79aeef2be8230',
          jobExecution: {
            id: 2,
            version: 2,
            createTime: '2018-08-20T22:20:47.051',
            startTime: '2018-08-20T22:20:47.054',
            endTime: '2018-08-20T22:20:53.706',
            status: 'COMPLETED',
            exitCode: 'COMPLETED',
            exitMessage: '',
            lastUpdated: '2018-08-20T22:20:53.707'
          }
        },
        {
          id: 1,
          version: 0,
          name: 'allRetrievalsJob',
          key: '72fc7f3a90c487f9a3f79aeef2be8230',
          jobExecution: {
            id: 1,
            version: 2,
            createTime: '2018-08-20T22:20:47.011',
            startTime: '2018-08-20T22:20:47.025',
            endTime: '2018-08-20T22:24:54.347',
            status: 'COMPLETED',
            exitCode: 'COMPLETED',
            exitMessage: '',
            lastUpdated: '2018-08-20T22:24:54.347'
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

    it('should get batch job instances', inject([AdminBatchService, HttpTestingController],
      (service: AdminBatchService, httpMock: HttpTestingController) => {
      const pageRequest: PageRequest = {};

      const requestMatch: RequestMatch = {
        method: 'GET',
        url: '/job-instances'
      };

      service.getBatchJobInstances(pageRequest).subscribe(paginatedBatchJobInstances => {
        expect(paginatedBatchJobInstances).toEqual(mockPaginatedJobInstances);
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.has(ADMIN_INTERCEPT_HEADER)).toBe(true);

      testReq.flush(mockPaginatedJobInstances);
    }));

    it('should get batch job instances with page', inject([AdminBatchService, HttpTestingController],
      (service: AdminBatchService, httpMock: HttpTestingController) => {
      const pageRequest: PageRequest = {
        page: 0,
      };

      const requestMatch: RequestMatch = {
        method: 'GET',
        url: `/job-instances?page=${pageRequest.page}`
      };

      service.getBatchJobInstances(pageRequest).subscribe(paginatedBatchJobInstances => {
        expect(paginatedBatchJobInstances).toEqual(mockPaginatedJobInstances);
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.has(ADMIN_INTERCEPT_HEADER)).toBe(true);

      testReq.flush(mockPaginatedJobInstances);
    }));

    it('should get batch job instances with page and size', inject([AdminBatchService, HttpTestingController],
      (service: AdminBatchService, httpMock: HttpTestingController) => {
      const pageRequest: PageRequest = {
        page: 0,
        size: 10
      };

      const requestMatch: RequestMatch = {
        method: 'GET',
        url: `/job-instances?page=${pageRequest.page}&size=${pageRequest.size}`
      };

      service.getBatchJobInstances(pageRequest).subscribe(paginatedBatchJobInstances => {
        expect(paginatedBatchJobInstances).toEqual(mockPaginatedJobInstances);
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.has(ADMIN_INTERCEPT_HEADER)).toBe(true);

      testReq.flush(mockPaginatedJobInstances);
    }));

    it('should get batch job instances with page, size, and sort', inject([AdminBatchService, HttpTestingController],
      (service: AdminBatchService, httpMock: HttpTestingController) => {
      const pageRequest: PageRequest = {
        sort: ['jobExecution.startTime,desc'],
        page: 0,
        size: 10
      };

      const requestMatch: RequestMatch = {
        method: 'GET',
        url: `/job-instances?page=${pageRequest.page}&size=${pageRequest.size}&sort=${pageRequest.sort[0]}`
      };

      service.getBatchJobInstances(pageRequest).subscribe(paginatedBatchJobInstances => {
        expect(paginatedBatchJobInstances).toEqual(mockPaginatedJobInstances);
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.has(ADMIN_INTERCEPT_HEADER)).toBe(true);

      testReq.flush(mockPaginatedJobInstances);
    }));

    it('should get batch job instances with REST error', inject([AdminBatchService, HttpTestingController],
      (service: AdminBatchService, httpMock: HttpTestingController) => {
      const pageRequest: PageRequest = {};

      const requestMatch: RequestMatch = {
        method: 'GET',
        url: '/job-instances'
      };

      service.getBatchJobInstances(pageRequest).subscribe(paginatedBatchJobInstances => {
        expect(paginatedBatchJobInstances).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.has(ADMIN_INTERCEPT_HEADER)).toBe(true);

      const errorEvent = document.createEvent('Event');
      errorEvent.initEvent('ErrorEvent', false, false);
      testReq.error(errorEvent as ErrorEvent);
    }));

  });

  describe('getStepExecutions', () => {

    const jobInstanceId = 1;

    const requestMatch: RequestMatch = {
      method: 'GET',
      url: `/job-instances/${jobInstanceId}/step-executions`
    };

    it('should get batch step executions', inject([AdminBatchService, HttpTestingController],
      (service: AdminBatchService, httpMock: HttpTestingController) => {
      const mockStepExecutions: BatchStepExecution[] = [
        {
          id: 3,
          version: 2,
          name: 'itemsRetrievalJobStep',
          startTime: '2018-08-20T22:20:47.054',
          endTime: '2018-08-20T22:22:14.517',
          status: 'COMPLETED',
          commitCount: 0,
          readCount: 0,
          filterCount: 0,
          writeCount: 0,
          readSkipCount: 0,
          writeSkipCount: 0,
          processSkipCount: 0,
          rollbackCount: 0,
          exitCode: 'COMPLETED',
          exitMessage: '',
          lastUpdated: '2018-08-20T22:22:14.518'
        }
      ];

      service.getStepExecutions(jobInstanceId).subscribe(batchStepExecutions => {
        expect(batchStepExecutions).toEqual(mockStepExecutions);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockStepExecutions);
    }));

    it('should get batch step executions with REST error', inject([AdminBatchService, HttpTestingController],
      (service: AdminBatchService, httpMock: HttpTestingController) => {
      service.getStepExecutions(jobInstanceId).subscribe(batchStepExecutions => {
        expect(batchStepExecutions).toEqual([]);
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.has(ADMIN_INTERCEPT_HEADER)).toBe(true);

      const errorEvent = document.createEvent('Event');
      errorEvent.initEvent('ErrorEvent', false, false);
      testReq.error(errorEvent as ErrorEvent);
    }));

  });

  describe('hasFailedAllRetrievalsJob', () => {

    const minutesAgo = 10;

    const requestMatch: RequestMatch = { method: 'GET', url: `/job-instances/has-failed-all-retrievals-job?minutes=${minutesAgo}` };

    it('should determine if has failed all retrievals job', inject([AdminBatchService, HttpTestingController],
      (service: AdminBatchService, httpMock: HttpTestingController) => {
      const mockHasFailedAllRetrievalsJob = {
        hasFailedAllRetrievalsJob: false
      };

      service.hasFailedAllRetrievalsJob(minutesAgo).subscribe(hasFailedAllRetrievalsJob => {
        expect(hasFailedAllRetrievalsJob).toBe(false);
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.has(ADMIN_INTERCEPT_HEADER)).toBe(true);

      testReq.flush(mockHasFailedAllRetrievalsJob);
    }));

    it('should determine if has failed all retrievals job with REST error', inject([AdminBatchService, HttpTestingController],
      (service: AdminBatchService, httpMock: HttpTestingController) => {
      service.hasFailedAllRetrievalsJob(minutesAgo).subscribe(hasFailedAllRetrievalsJob => {
        expect(hasFailedAllRetrievalsJob).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.has(ADMIN_INTERCEPT_HEADER)).toBe(true);

      const errorEvent = document.createEvent('Event');
      errorEvent.initEvent('ErrorEvent', false, false);
      testReq.error(errorEvent as ErrorEvent);
    }));

  });

  describe('restartAllRetrievalsJob', () => {

    const requestMatch: RequestMatch = { method: 'POST', url: '/job-instances/restart' };

    it('should restart all retrievals job', inject([AdminBatchService, HttpTestingController],
      (service: AdminBatchService, httpMock: HttpTestingController) => {
      const mockBatchJobExecution: BatchJobExecution = {
        id: 163,
        version: 2,
        createTime: '2018-12-19T22:35:35.586',
        startTime: '2018-12-19T22:35:35.6',
        endTime: '2018-12-19T22:36:08.692',
        status: 'COMPLETED',
        exitCode: 'COMPLETED',
        exitMessage: '',
        lastUpdated: '2018-12-19T22:36:08.692'
      };

      service.restartAllRetrievalsJob().subscribe(restartAllRetrievalsJob => {
        expect(restartAllRetrievalsJob).toEqual(mockBatchJobExecution);
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.has(ADMIN_INTERCEPT_HEADER)).toBe(true);

      testReq.flush(mockBatchJobExecution);
    }));

    it('should restart all retrievals job with REST error', inject([AdminBatchService, HttpTestingController],
      (service: AdminBatchService, httpMock: HttpTestingController) => {
      service.restartAllRetrievalsJob().subscribe(restartAllRetrievalsJob => {
        expect(restartAllRetrievalsJob).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.headers.has(ADMIN_INTERCEPT_HEADER)).toBe(true);

      const errorEvent = document.createEvent('Event');
      errorEvent.initEvent('ErrorEvent', false, false);
      testReq.error(errorEvent as ErrorEvent);
    }));

  });

});
