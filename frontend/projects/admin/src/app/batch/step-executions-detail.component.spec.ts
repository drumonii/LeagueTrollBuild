import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { StepExecutionsDetailModule } from './step-executions-detail.module';
import { StepExecutionsDetailComponent } from './step-executions-detail.component';
import { StepExecutionsDetailService } from './step-executions-detail.service';
import { BatchStepExecution } from './batch-job';

describe('StepExecutionsDetailComponent', () => {
  let component: StepExecutionsDetailComponent;
  let fixture: ComponentFixture<StepExecutionsDetailComponent>;

  const jobExecutionId = 2;

  const stepExecutions: BatchStepExecution[] = [
    {
      id: 26,
      version: 8,
      name: 'championsRetrievalStep',
      startTime: '2019-07-08T23:30:51.116',
      endTime: '2019-07-08T23:33:54.902',
      status: 'COMPLETED',
      commitCount: 6,
      readCount: 145,
      filterCount: 72,
      writeCount: 73,
      readSkipCount: 0,
      writeSkipCount: 0,
      processSkipCount: 0,
      rollbackCount: 0,
      exitCode: 'COMPLETED',
      exitMessage: '',
      lastUpdated: '2019-07-08T23:33:54.902'
    },
    {
      id: 27,
      version: 15,
      name: 'itemsRetrievalStep',
      startTime: '2019-07-08T23:30:51.116',
      endTime: '2019-07-08T23:32:16.402',
      status: 'COMPLETED',
      commitCount: 13,
      readCount: 319,
      filterCount: 0,
      writeCount: 319,
      readSkipCount: 0,
      writeSkipCount: 0,
      processSkipCount: 0,
      rollbackCount: 0,
      exitCode: 'COMPLETED',
      exitMessage: '',
      lastUpdated: '2019-07-08T23:32:16.403'
    },
    {
      id: 28,
      version: 12,
      name: 'versionsRetrievalStep',
      startTime: '2019-07-08T23:30:51.123',
      endTime: '2019-07-08T23:30:51.33',
      status: 'COMPLETED',
      commitCount: 10,
      readCount: 225,
      filterCount: 223,
      writeCount: 2,
      readSkipCount: 0,
      writeSkipCount: 0,
      processSkipCount: 0,
      rollbackCount: 0,
      exitCode: 'COMPLETED',
      exitMessage: '',
      lastUpdated: '2019-07-08T23:30:51.33'
    },
    {
      id: 30,
      version: 3,
      name: 'mapsRetrievalStep',
      startTime: '2019-07-08T23:30:51.123',
      endTime: '2019-07-08T23:30:51.925',
      status: 'COMPLETED',
      commitCount: 1,
      readCount: 4,
      filterCount: 3,
      writeCount: 1,
      readSkipCount: 0,
      writeSkipCount: 0,
      processSkipCount: 0,
      rollbackCount: 0,
      exitCode: 'COMPLETED',
      exitMessage: '',
      lastUpdated: '2019-07-08T23:30:51.926'
    },
    {
      id: 29,
      version: 3,
      name: 'summonerSpellsRetrievalStep',
      startTime: '2019-07-08T23:30:51.122',
      endTime: '2019-07-08T23:30:51.411',
      status: 'COMPLETED',
      commitCount: 1,
      readCount: 16,
      filterCount: 16,
      writeCount: 0,
      readSkipCount: 0,
      writeSkipCount: 0,
      processSkipCount: 0,
      rollbackCount: 0,
      exitCode: 'COMPLETED',
      exitMessage: '',
      lastUpdated: '2019-07-08T23:30:51.411'
    }
  ];

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, StepExecutionsDetailModule]
    })
    .compileComponents();
  }));

  beforeEach(inject([StepExecutionsDetailService], (batchService: StepExecutionsDetailService) => {
    fixture = TestBed.createComponent(StepExecutionsDetailComponent);
    component = fixture.componentInstance;

    component.jobExecutionId = jobExecutionId;

    const batchServiceSpy = spyOn(batchService, 'getStepExecutions');
    for (let i = 0; i < component.NEXT_JOB_EXECUTION_IDS; i++) {
      batchServiceSpy.and.returnValue(of([stepExecutions[i]]));
    }

    fixture.detectChanges();
  }));

  afterEach(inject([StepExecutionsDetailService], (batchService: StepExecutionsDetailService) => {
    expect(batchService.getStepExecutions).toHaveBeenCalledTimes(component.NEXT_JOB_EXECUTION_IDS);
    expect(batchService.getStepExecutions).toHaveBeenCalledWith(3);
    expect(batchService.getStepExecutions).toHaveBeenCalledWith(4);
    expect(batchService.getStepExecutions).toHaveBeenCalledWith(5);
    expect(batchService.getStepExecutions).toHaveBeenCalledWith(6);
    expect(batchService.getStepExecutions).toHaveBeenCalledWith(7);
  }));

  it('should get step executions for the job execution id', () => {
    expect(fixture.debugElement.query(By.css('.step-executions-table'))).toBeTruthy();
  });
});
