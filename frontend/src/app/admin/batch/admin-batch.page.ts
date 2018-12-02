import { Component, OnInit } from '@angular/core';

import { finalize } from 'rxjs/operators';

import { AdminBatchService } from './admin-batch.service';
import { TitleService } from '@service/title.service';
import { BatchJobInstance } from '@admin-model/batch-job-instance';
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

  loadingIndicator: boolean;

  rows: BatchJobInstance[];

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

  getStatusCss({ row, column, value }): DatatableCss {
    return {
      'has-text-success': value === 'COMPLETED',
      'has-text-danger': value === 'FAILED',
      'has-text-warning': value === 'STARTED'
    };
  }

}
