import { Component, OnInit, ViewChild } from '@angular/core';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AdminBatchService } from './admin-batch.service';
import { TitleService } from '@service/title.service';
import { BatchJobInstance } from '@admin-model/batch-job-instance';
import { BatchStepExecution } from '@admin-model/batch-step-execution';
import { PageRequest } from '@admin-model/page-request';
import { DatatableSort, DatatableSorts } from '@admin-model/datatable-sort';
import { DatatablePage } from '@admin-model/datatable-page';
import { DatatableCss } from '@admin-model/datatable-css';

@Component({
  selector: 'ltb-admin-batch',
  templateUrl: './admin-batch.page.html',
  styleUrls: ['./admin-batch.page.scss']
})
export class AdminBatchPage implements OnInit {

  @ViewChild('batchJobsDatatable') table: any;

  loadingIndicator: boolean;

  rows: BatchJobInstance[];

  expanded = {};

  page: DatatablePage = {
    limit: 20,
    offset: 0
  };

  sorts: DatatableSorts[] = [
    {
      prop: 'jobExecution.startTime',
      dir: 'desc'
    }
  ];

  constructor(private batchService: AdminBatchService, private titleService: TitleService) {}

  ngOnInit() {
    this.setTitle();

    this.setPage(this.page);
  }

  private setTitle() {
    this.titleService.setTitle('Batch Jobs');
  }

  getBatchJobInstances(pageRequest: PageRequest): void {
    this.loadingIndicator = true;
    this.batchService.getBatchJobInstances(pageRequest)
      .pipe(
        finalize(() => this.loadingIndicator = false)
      )
      .subscribe((paginatedBatchJobInstances) => {
        this.rows = paginatedBatchJobInstances.content;
        this.page.count = paginatedBatchJobInstances.totalElements;
      });
  }

  getStepExecutions(jobInstanceId: number): Observable<BatchStepExecution[]> {
    return this.batchService.getStepExecutions(jobInstanceId);
  }

  setPage(pageInfo: DatatablePage): void {
    this.getBatchJobInstances({ page: pageInfo.offset, size: pageInfo.limit, sort: this.getSorts() });
  }

  setSort(sortInfo: DatatableSort): void {
    const sorts: DatatableSorts[] = [];
    for (const sort of sortInfo.sorts) {
      sorts.push({ prop: sort.prop, dir: sort.dir });
    }
    this.sorts = sorts;
    this.getBatchJobInstances({ page: this.page.offset, size: this.page.limit, sort: this.getSorts() });
  }

  getSorts(): string[] {
    const sorts = [];
    for (const sort of this.sorts) {
      sorts.push(`${sort.prop},${sort.dir}`);
    }
    return sorts;
  }

  getStatusCss({ row, column, value }): DatatableCss | string {
    if (row && column && value) {
      return {
        'has-text-success': value === 'COMPLETED',
        'has-text-danger': value === 'FAILED',
        'has-text-warning': value === 'STARTED'
      };
    }
    switch (value) {
      case 'COMPLETED':
        return 'has-text-success';
      case 'FAILED':
        return 'has-text-danger';
      case 'STARTED':
        return 'has-text-warning';
      default:
        return '';
    }
  }

  toggleExpandRow(row): void {
    if (!row.jobExecution.stepExecutions) {
      this.getStepExecutions(row.id).subscribe((stepExecutions) => {
        row.jobExecution.stepExecutions = stepExecutions;
      });
    }
    this.table.rowDetail.toggleExpandRow(row);
  }

  getCompletionTime(startTime: string, endTime: string): string {
    if (!endTime) {
      return '';
    }
    const startDate = new Date(startTime);
    const endDate = new Date(endTime);
    const diffInMs = Math.abs(endDate.getTime() - startDate.getTime());
    const diffInMinutes = (diffInMs / 1000) / 60;
    return `${diffInMinutes.toFixed(2)} min`;
  }

}
