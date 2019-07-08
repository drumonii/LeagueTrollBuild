import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { StepExecutionsDetailService } from './step-executions-detail.service';
import { BatchStepExecution } from './batch-job';

describe('StepExecutionsDetailService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [StepExecutionsDetailService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getStepExecutions', () => {

    const jobInstanceId = 1;

    const requestMatch: RequestMatch = {
      method: 'GET',
      url: `/job-instances/${jobInstanceId}/step-executions`
    };

    it('should get batch step executions', inject([StepExecutionsDetailService, HttpTestingController],
      (service: StepExecutionsDetailService, httpMock: HttpTestingController) => {
      const versionsRetrievalJobStep: BatchStepExecution = {
        id: 4,
        version: 2,
        name: 'versionsRetrievalJobStep',
        startTime: '2019-03-21T22:24:55.742',
        endTime: '2019-03-21T22:25:24.279',
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
        lastUpdated: '2019-03-21T22:25:24.279'
      };
      const mapsRetrievalJobStep: BatchStepExecution = {
        id: 1,
        version: 2,
        name: 'mapsRetrievalJobStep',
        startTime: '2019-03-21T22:24:55.733',
        endTime: '2019-03-21T22:25:25.566',
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
        lastUpdated: '2019-03-21T22:25:25.566'
      };

      const mockStepExecutions: BatchStepExecution[] = [versionsRetrievalJobStep, mapsRetrievalJobStep];

      service.getStepExecutions(jobInstanceId).subscribe(batchStepExecutions => {
        expect(batchStepExecutions).toEqual([mapsRetrievalJobStep, versionsRetrievalJobStep]);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockStepExecutions);
    }));

    it('should get batch step executions with REST error', inject([StepExecutionsDetailService, HttpTestingController],
      (service: StepExecutionsDetailService, httpMock: HttpTestingController) => {
      service.getStepExecutions(jobInstanceId).subscribe(batchStepExecutions => {
        expect(batchStepExecutions).toEqual([]);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });
});
