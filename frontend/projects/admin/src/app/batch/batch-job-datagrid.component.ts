import { ChangeDetectionStrategy, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';

import { ClrDatagridPagination, ClrDatagridStateInterface } from '@clr/angular';

import { Observable, Subscription } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BatchJobDatagridService } from './batch-job-datagrid.service';
import { DatagridFilter, PageRequest } from './datagrid';
import { BatchJobInstance } from './batch-job';

@Component({
  selector: 'ltb-admin-batch-job-datagrid',
  templateUrl: './batch-job-datagrid.component.html',
  styleUrls: ['./batch-job-datagrid.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BatchJobDatagridComponent implements OnInit, OnDestroy {

  @ViewChild('pagination', { static: true }) pagination: ClrDatagridPagination;

  jobInstances$: Observable<BatchJobInstance[]>;
  total: number;
  loading: boolean;

  hasFailedAllRetrievalsJob$: Observable<boolean>;
  minutesAgo = 5;
  restartingAllRetrievalsJob: boolean;

  private subscriptions = new Subscription();

  constructor(private batchService: BatchJobDatagridService) {}

  ngOnInit() {
    this.checkRecentLatestAllRetrievalsJob();
  }

  getBatchJobInstances(pageRequest: PageRequest): void {
    this.loading = true;
    this.jobInstances$ = this.batchService.getBatchJobInstances(pageRequest)
      .pipe(
        map((paginatedBatchJobInstances) => {
          if (paginatedBatchJobInstances) {
            this.total = paginatedBatchJobInstances.totalElements;
            return paginatedBatchJobInstances.content;
          }
          this.total = 0;
          return [];
        }),
        finalize(() => this.loading = false)
      );
  }

  checkRecentLatestAllRetrievalsJob(): void {
    this.hasFailedAllRetrievalsJob$ = this.batchService.hasFailedAllRetrievalsJob(this.minutesAgo);
  }

  restartAllRetrievalsJob(): void {
    this.restartingAllRetrievalsJob = true;
    this.subscriptions.add(this.batchService.restartAllRetrievalsJob()
      .pipe(
        finalize(() => {
          this.restartingAllRetrievalsJob = false;
          this.getBatchJobInstances({
            page: this.pagination.page.current - 1,
            size: this.pagination.page.size,
            filters: [{
              property: 'name',
              value: 'allRetrievalsJob'
            }],
            sort: ['jobExecution.startTime,desc']
          });
          this.checkRecentLatestAllRetrievalsJob();
        })
      )
      .subscribe());
  }

  refresh(state: ClrDatagridStateInterface): void {
    const sort = [];
    if (state.sort) {
      sort.push(`${state.sort.by},${state.sort.reverse ? 'desc' : 'asc'}`);
    }
    const filters: DatagridFilter[] = [{
      property: 'name',
      value: 'allRetrievalsJob'
    }];
    if (state.filters) {
      for (const filter of state.filters) {
        filters.push({ property: filter.property, value: filter.value });
      }
    }
    let page = 0;
    let size = 10;
    if (state.page) {
      page = this.pagination.page.current - 1;
      size = state.page.size;
    }
    const pageRequest: PageRequest = { page, size, sort, filters };
    this.getBatchJobInstances(pageRequest);
  }

  trackByJobInstance(index: number, jobInstance: BatchJobInstance): number {
    return jobInstance.id;
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
