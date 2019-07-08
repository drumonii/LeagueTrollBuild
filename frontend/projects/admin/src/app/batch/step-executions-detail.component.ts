import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';

import { forkJoin, Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BatchStepExecution } from './batch-job';
import { StepExecutionsDetailService } from './step-executions-detail.service';

@Component({
  selector: 'ltb-admin-step-executions-detail',
  templateUrl: './step-executions-detail.component.html',
  styleUrls: ['./step-executions-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StepExecutionsDetailComponent implements OnInit {

  NEXT_JOB_EXECUTION_IDS = 5;

  @Input()
  jobExecutionId: number;

  loading: boolean;
  stepExecutions$: Observable<BatchStepExecution[]>;

  constructor(private stepExecutionsDetailService: StepExecutionsDetailService) {}

  ngOnInit() {
    this.getStepExecutions();
  }

  getStepExecutions(): void {
    this.loading = true;

    const allStepExecutions$: Observable<BatchStepExecution[]>[] = this.getNextJobExecutionIds()
      .map(jobExecutionId => this.stepExecutionsDetailService.getStepExecutions(jobExecutionId));

    this.stepExecutions$ = forkJoin(allStepExecutions$)
      .pipe(
        finalize(() => this.loading = false),
        map((stepExecutions) => [].concat(...stepExecutions)),
        map((stepExecutions) => stepExecutions.sort((s1, s2) => s1.id - s2.id)),
      );
  }

  private getNextJobExecutionIds(): number[] {
    // there are a set number of job execution ids until the next allRetrievalsJob occurrence
    const nextJobExecutionIds = [];
    for (let i = this.jobExecutionId + 1; i <= this.jobExecutionId + this.NEXT_JOB_EXECUTION_IDS; i++) {
      nextJobExecutionIds.push(i);
    }
    return nextJobExecutionIds;
  }

  trackByStepExecution(index: number, batchStepExecution: BatchStepExecution): number {
    return batchStepExecution.id;
  }

}
