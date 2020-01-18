import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { BatchJobDatagridService } from './batch-job-datagrid.service';
import { BatchJobExecution, BatchJobInstance } from './batch-job';
import { PageRequest, Paginated } from './datagrid';

describe('BatchJobDatagridService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [BatchJobDatagridService]
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

    it('should get batch job instances', inject([BatchJobDatagridService, HttpTestingController],
      (service: BatchJobDatagridService, httpMock: HttpTestingController) => {
      const pageRequest: PageRequest = {};

      const requestMatch: RequestMatch = {
        method: 'GET',
        url: '/job-instances'
      };

      service.getBatchJobInstances(pageRequest).subscribe(paginatedBatchJobInstances => {
        expect(paginatedBatchJobInstances).toEqual(mockPaginatedJobInstances);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockPaginatedJobInstances);
    }));

    it('should get batch job instances with page', inject([BatchJobDatagridService, HttpTestingController],
      (service: BatchJobDatagridService, httpMock: HttpTestingController) => {
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

      testReq.flush(mockPaginatedJobInstances);
    }));

    it('should get batch job instances with page and size', inject([BatchJobDatagridService, HttpTestingController],
      (service: BatchJobDatagridService, httpMock: HttpTestingController) => {
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

      testReq.flush(mockPaginatedJobInstances);
    }));

    it('should get batch job instances with page, size, and sort', inject([BatchJobDatagridService, HttpTestingController],
      (service: BatchJobDatagridService, httpMock: HttpTestingController) => {
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

      testReq.flush(mockPaginatedJobInstances);
    }));

    it('should get batch job instances with page, size, sort, and filters', inject([BatchJobDatagridService, HttpTestingController],
      (service: BatchJobDatagridService, httpMock: HttpTestingController) => {
      const pageRequest: PageRequest = {
        sort: ['jobExecution.startTime,desc'],
        filters: [
          {
            property: 'name',
            value: 'test'
          }
        ],
        page: 0,
        size: 10
      };

      const requestMatch: RequestMatch = {
        method: 'GET',
        url: `/job-instances?page=${pageRequest.page}&size=${pageRequest.size}&sort=${pageRequest.sort[0]}&name=test`
      };

      service.getBatchJobInstances(pageRequest).subscribe(paginatedBatchJobInstances => {
        expect(paginatedBatchJobInstances).toEqual(mockPaginatedJobInstances);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockPaginatedJobInstances);
    }));

    it('should get batch job instances with REST error', inject([BatchJobDatagridService, HttpTestingController],
      (service: BatchJobDatagridService, httpMock: HttpTestingController) => {
      const pageRequest: PageRequest = {};

      const requestMatch: RequestMatch = {
        method: 'GET',
        url: '/job-instances'
      };

      service.getBatchJobInstances(pageRequest).subscribe(paginatedBatchJobInstances => {
        expect(paginatedBatchJobInstances).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('hasFailedAllRetrievalsJob', () => {

    const minutesAgo = 10;

    const requestMatch: RequestMatch = { method: 'GET', url: `/job-instances/has-failed-all-retrievals-job?minutes=${minutesAgo}` };

    it('should determine if has failed all retrievals job', inject([BatchJobDatagridService, HttpTestingController],
      (service: BatchJobDatagridService, httpMock: HttpTestingController) => {
      const mockHasFailedAllRetrievalsJob = {
        hasFailedAllRetrievalsJob: false
      };

      service.hasFailedAllRetrievalsJob(minutesAgo).subscribe(hasFailedAllRetrievalsJob => {
        expect(hasFailedAllRetrievalsJob).toBeFalse();
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockHasFailedAllRetrievalsJob);
    }));

    it('should determine if has failed all retrievals job with REST error', inject([BatchJobDatagridService, HttpTestingController],
      (service: BatchJobDatagridService, httpMock: HttpTestingController) => {
      service.hasFailedAllRetrievalsJob(minutesAgo).subscribe(hasFailedAllRetrievalsJob => {
        expect(hasFailedAllRetrievalsJob).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('restartAllRetrievalsJob', () => {

    const requestMatch: RequestMatch = { method: 'POST', url: '/job-instances/restart' };

    it('should restart all retrievals job', inject([BatchJobDatagridService, HttpTestingController],
      (service: BatchJobDatagridService, httpMock: HttpTestingController) => {
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

      testReq.flush(mockBatchJobExecution);
    }));

    it('should restart all retrievals job with REST error', inject([BatchJobDatagridService, HttpTestingController],
      (service: BatchJobDatagridService, httpMock: HttpTestingController) => {
      service.restartAllRetrievalsJob().subscribe(restartAllRetrievalsJob => {
        expect(restartAllRetrievalsJob).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });
});
