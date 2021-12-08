import { ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { DebugElement } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { BatchJobDatagridModule } from './batch-job-datagrid.module';
import { BatchJobDatagridComponent } from './batch-job-datagrid.component';
import { BatchJobDatagridService } from './batch-job-datagrid.service';
import { BatchJobExecution, BatchJobInstance } from './batch-job';
import { Paginated } from './datagrid';

describe('BatchJobDatagridComponent', () => {
  let component: BatchJobDatagridComponent;
  let fixture: ComponentFixture<BatchJobDatagridComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NoopAnimationsModule, BatchJobDatagridModule]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BatchJobDatagridComponent);
    component = fixture.componentInstance;
  });

  describe('batch jobs datatable', () => {

    const batchJobInstances: Paginated<BatchJobInstance> = {
      content: [
        {
          id: 3,
          version: 0,
          name: 'championsRetrievalJob',
          key: '72fc7f3a90c487f9a3f79aeef2be8230',
          jobExecution: {
            id: 3,
            version: 2,
            createTime: '2018-11-02T20:26:21.475',
            startTime: '2018-11-02T20:26:21.478',
            endTime: null,
            status: 'STARTED',
            exitCode: 'STARTED',
            exitMessage: '',
            lastUpdated: '2018-11-02T20:31:29.558'
          }
        },
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
            status: 'FAILED',
            exitCode: 'FAILED',
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
      totalElements: 3,
      totalPages: 1,
      last: true,
      number: 0,
      size: 20,
      sort: {
        sorted: true,
        unsorted: false,
        empty: false
      },
      numberOfElements: 3,
      first: true,
      empty: false
    };

    const tableHeaders = [
      { title: 'Job Id' },
      { title: 'Name' },
      { title: 'Status' },
      { title: 'Start Time' },
      { title: 'End Time' },
      { title: 'Completion Time' }
    ];

    const headersIndexes = {
      id: 0,
      name: 1,
      status: 2,
      startTime: 3,
      endTime: 4,
      completionTime: 5
    };

    it('should load batch jobs server side', () => {
      // workaround for async pipe not subscribing in the clr-dg-row
      component.jobInstances$ = of(batchJobInstances.content);
      component.total = batchJobInstances.totalElements;

      fixture.detectChanges();

      expectBatchJobsDatatable();
    });

    describe('pagination', () => {

      beforeEach(inject([BatchJobDatagridService], (batchService: BatchJobDatagridService) => {
        spyOn(batchService, 'getBatchJobInstances').and.returnValue(of(batchJobInstances));

        fixture.detectChanges();
      }));

      it('should paginate batch jobs server side', inject([BatchJobDatagridService], (batchService: BatchJobDatagridService) => {
        expectInitialCall(batchService);

        component.pagination.page.size = 1; // to simplify paging with a small amount of test data

        fixture.detectChanges();

        const nextPageLink = fixture.debugElement.query(By.css('.pagination-next'));
        nextPageLink.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances).toHaveBeenCalledWith({
          page: 1,
          size: 1,
          filters: [{
            property: 'name',
            value: 'allRetrievalsJob'
          }],
          sort: ['jobExecution.startTime,desc']
        });
      }));

    });

    describe('sorting', () => {

      beforeEach(inject([BatchJobDatagridService], (batchService: BatchJobDatagridService) => {
        spyOn(batchService, 'getBatchJobInstances').and.returnValue(of(batchJobInstances));

        fixture.detectChanges();
      }));

      it('should sort batch jobs by id server side', inject([BatchJobDatagridService], (batchService: BatchJobDatagridService) => {
        expectInitialCall(batchService);

        clickHeaderColumn('Job Id');

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances).toHaveBeenCalledWith({
          page: 0,
          size: 20,
          filters: [{
            property: 'name',
            value: 'allRetrievalsJob'
          }],
          sort: ['id,asc']
        });
      }));

      it('should sort batch jobs by name server side', inject([BatchJobDatagridService], (batchService: BatchJobDatagridService) => {
        expectInitialCall(batchService);

        clickHeaderColumn('Name');

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances).toHaveBeenCalledWith({
          page: 0,
          size: 20,
          filters: [{
            property: 'name',
            value: 'allRetrievalsJob'
          }],
          sort: ['name,asc']
        });
      }));

      it('should sort batch jobs by status server side', inject([BatchJobDatagridService], (batchService: BatchJobDatagridService) => {
        expectInitialCall(batchService);

        clickHeaderColumn('Status');

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances).toHaveBeenCalledWith({
          page: 0,
          size: 20,
          filters: [{
            property: 'name',
            value: 'allRetrievalsJob'
          }],
          sort: ['jobExecution.status,asc']
        });
      }));

      it('should sort batch jobs by start time server side', inject([BatchJobDatagridService], (batchService: BatchJobDatagridService) => {
        expectInitialCall(batchService);

        clickHeaderColumn('Start Time');

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances).toHaveBeenCalledWith({
          page: 0,
          size: 20,
          filters: [{
            property: 'name',
            value: 'allRetrievalsJob'
          }],
          sort: ['jobExecution.startTime,asc']
        });
      }));

      it('should sort batch jobs by end time server side', inject([BatchJobDatagridService], (batchService: BatchJobDatagridService) => {
        expectInitialCall(batchService);

        clickHeaderColumn('End Time');

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances).toHaveBeenCalledWith({
          page: 0,
          size: 20,
          filters: [{
            property: 'name',
            value: 'allRetrievalsJob'
          }],
          sort: ['jobExecution.endTime,asc']
        });
      }));

      it('should not sort batch jobs by completion time server side',
        inject([BatchJobDatagridService], (batchService: BatchJobDatagridService) => {
        expectInitialCall(batchService);

        clickHeaderColumn('Completion Time');

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances).toHaveBeenCalledTimes(1);
      }));

      it('should refresh batch jobs server side', inject([BatchJobDatagridService], (batchService: BatchJobDatagridService) => {
        expectInitialCall(batchService);

        const refreshDatatableBtn = fixture.debugElement.query(By.css('[data-e2e="refresh-failed-jobs-btn"]'));
        refreshDatatableBtn.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances).toHaveBeenCalledWith({
          page: 0,
          size: 20,
          filters: [{
            property: 'name',
            value: 'allRetrievalsJob'
          }],
          sort: ['jobExecution.startTime,desc']
        });
      }));

      function clickHeaderColumn(title: string) {
        const headers = fixture.debugElement.queryAll(By.css('clr-dg-column'));
        const idHeader = headers[tableHeaders.findIndex((header) => header.title === title)];
        const idHeaderLabel = idHeader.query(By.css('button.datagrid-column-title'));
        if (idHeaderLabel) {
          idHeaderLabel.triggerEventHandler('click', null);
        } else {
          expect(idHeader.query(By.css('span.datagrid-column-title'))).toBeTruthy();
        }
      }

    });

    function expectBatchJobsDatatable() {
      expect(fixture.debugElement.query(By.css('[data-e2e="batch-jobs-datatable"]'))).toBeTruthy();
      const dataTableHeaders = fixture.debugElement.queryAll(By.css('clr-dg-column'));
      expect(dataTableHeaders.length).toBe(tableHeaders.length);
      for (let i = 1; i < dataTableHeaders.length; i++) { // skip the expand column
        const header = dataTableHeaders[i];
        const headerLabel = header.query(By.css('.datagrid-column-title'));
        expect(headerLabel.nativeElement.textContent.trim()).toBe(tableHeaders[i].title);
      }
      const rows = fixture.debugElement.queryAll(By.css('clr-dg-row'));
      expect(rows.length).toBe(batchJobInstances.content.length);

      // championsRetrievalJob - STARTED row
      const batchJobRow0 = getRow('championsRetrievalJob');
      const batchJob0Columns = getColumns(batchJobRow0);
      expectStartedRow(batchJob0Columns);

      // versionsRetrievalJob - FAILED row
      const batchJobRow1 = getRow('versionsRetrievalJob');
      const batchJob1Columns = getColumns(batchJobRow1);
      expectFailedRow(batchJob1Columns);

      // allRetrievalsJob - COMPLETED row
      const batchJobRow2 = getRow('allRetrievalsJob');
      const batchJob2Columns = getColumns(batchJobRow2);
      expectCompletedRow(batchJob2Columns);
    }

    function getColumns(championsRetrievalJobRow: DebugElement): DebugElement[] {
      return championsRetrievalJobRow.queryAll(By.css('clr-dg-cell'));
    }

    function getRow(jobInstanceName: string) {
      const rows = fixture.debugElement.queryAll(By.css('clr-dg-row'));
      return rows[batchJobInstances.content.findIndex(jobInstance => jobInstance.name === jobInstanceName)];
    }

    function getStatusColumn(columns: DebugElement[]) {
      return columns[headersIndexes.status];
    }

    function getCompletionTimeColumn(columns: DebugElement[]) {
      return columns[headersIndexes.completionTime];
    }

    function expectStartedRow(columns: DebugElement[]) {
      const statusColumn = getStatusColumn(columns);
      expect(statusColumn.nativeElement.textContent.trim()).toBe('STARTED');
      expect(statusColumn.query(By.css('clr-icon'))).toBeFalsy();

      const completionTimeColumn = getCompletionTimeColumn(columns);
      expect(completionTimeColumn.nativeElement.textContent.trim()).toBe('');
    }

    function expectFailedRow(columns: DebugElement[]) {
      const statusColumn = getStatusColumn(columns);
      expect(statusColumn.nativeElement.textContent.trim()).toBe('FAILED');
      expect(statusColumn.query(By.css('clr-icon.is-error'))).toBeTruthy();

      const completionTimeColumn = getCompletionTimeColumn(columns);
      expect(completionTimeColumn.nativeElement.textContent.trim()).toBe('0.11 min');
    }

    function expectCompletedRow(columns: DebugElement[]) {
      const statusColumn = getStatusColumn(columns);
      expect(statusColumn.nativeElement.textContent.trim()).toBe('COMPLETED');
      expect(statusColumn.query(By.css('clr-icon.is-success'))).toBeTruthy();

      const completionTimeColumn = getCompletionTimeColumn(columns);
      expect(completionTimeColumn.nativeElement.textContent.trim()).toBe('4.12 min');
    }

  });

  describe('has failed all retrievals job', () => {

    const batchJobInstances: Paginated<BatchJobInstance> = {
      content: [],
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
      totalElements: 0,
      totalPages: 0,
      last: true,
      number: 0,
      size: 20,
      sort: {
        sorted: true,
        unsorted: false,
        empty: false
      },
      numberOfElements: 0,
      first: true,
      empty: false
    };

    const batchJobExecution: BatchJobExecution = {
      id: 1,
      version: 1,
      createTime: '2018-12-19T10:22:35.586',
      startTime: '2018-12-10:22:35.6',
      endTime: '2018-12-10:22:08.692',
      status: 'COMPLETED',
      exitCode: 'COMPLETED',
      exitMessage: '',
      lastUpdated: '2018-12-10:22:08.692'
    };

    beforeEach(inject([BatchJobDatagridService], (batchService: BatchJobDatagridService) => {
      spyOn(batchService, 'getBatchJobInstances').and.returnValue(of(batchJobInstances));
      spyOn(batchService, 'hasFailedAllRetrievalsJob').and.returnValue(of(true));
      spyOn(batchService, 'restartAllRetrievalsJob').and.returnValue(of(batchJobExecution));

      fixture.detectChanges();
    }));

    it('should show the recent failed all retrievals job alert',
      inject([BatchJobDatagridService], (batchService: BatchJobDatagridService) => {
      expect(fixture.debugElement.query(By.css('[data-e2e="failed-job-alert"]'))).toBeTruthy();

      const restartJobBtn = fixture.debugElement.query(By.css('[data-e2e="restart-job-btn"]'));
      expect(restartJobBtn.nativeElement.textContent.trim()).toBe('Restart');
      restartJobBtn.triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(batchService.restartAllRetrievalsJob).toHaveBeenCalled();
      expect(batchService.hasFailedAllRetrievalsJob).toHaveBeenCalledTimes(2);
      expect(batchService.hasFailedAllRetrievalsJob).toHaveBeenCalledWith(component.minutesAgo);
      expect(batchService.getBatchJobInstances).toHaveBeenCalledWith({
        page: 0,
        size: 20,
        filters: [{
          property: 'name',
          value: 'allRetrievalsJob'
        }],
        sort: ['jobExecution.startTime,desc']
      });
    }));

  });

  function expectInitialCall(batchService: BatchJobDatagridService) {
    expect(batchService.getBatchJobInstances).toHaveBeenCalledWith({
      page: 0,
      size: 20,
      filters: [{
        property: 'name',
        value: 'allRetrievalsJob'
      }],
      sort: ['jobExecution.startTime,desc']
    });
  }

});
