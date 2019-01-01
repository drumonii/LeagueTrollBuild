import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { FailedJobsService } from './failed-jobs.service';
import { Paginated } from '@admin-model/paginated';
import { BatchJobInstance } from '@admin-model/batch-job-instance';

describe('FailedJobsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [FailedJobsService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getFailedJobs', () => {

    const requestMatch: RequestMatch = { method: 'GET', url: '/job-instances?jobExecution.status=FAILED' };

    it('should get failed jobs', inject([FailedJobsService, HttpTestingController],
      (service: FailedJobsService, httpMock: HttpTestingController) => {
      const mockFailedJobInstances: Paginated<BatchJobInstance> = {
        content: [], // content omitted for brevity
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
        expect(failedJobs).toBe(mockFailedJobInstances.totalElements);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockFailedJobInstances);
    }));

    it('should get failed jobs with REST error', inject([FailedJobsService, HttpTestingController],
      (service: FailedJobsService, httpMock: HttpTestingController) => {
      service.getFailedJobs().subscribe(failedJobs => {
        expect(failedJobs).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);

      const errorEvent = document.createEvent('Event');
      errorEvent.initEvent('ErrorEvent', false, false);
      testReq.error(errorEvent as ErrorEvent);
    }));

  });

});
