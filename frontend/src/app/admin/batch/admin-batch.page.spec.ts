import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { AdminBatchModule } from './admin-batch.module';
import { AdminBatchPage } from './admin-batch.page';
import { AdminBatchService } from './admin-batch.service';
import { AdminTitleService } from '@admin-service/admin-title.service';
import { BatchJobInstance } from '@admin-model/batch-job-instance';
import { Paginated } from '@admin-model/paginated';
import { BatchStepExecution } from '@admin-model/batch-step-execution';
import { BatchJobExecution } from '@admin-model/batch-job-execution';

describe('AdminBatchPage', () => {
  let component: AdminBatchPage;
  let fixture: ComponentFixture<AdminBatchPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdminBatchModule, HttpClientTestingModule]
    })
    .compileComponents();
  }));

  beforeEach(inject([AdminTitleService], (title: AdminTitleService) => {
    fixture = TestBed.createComponent(AdminBatchPage);
    component = fixture.componentInstance;

    spyOn(title, 'setTitle').and.callThrough();
  }));

  afterEach(inject([AdminTitleService], (title: AdminTitleService) => {
    expect(title.setTitle).toHaveBeenCalledWith('Batch Jobs');
  }));

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

    const headersIndexes = { rowDetail: 0, id: 1, name: 2, status: 3, startTime: 4, endTime: 5, completionTime: 6 };

    beforeEach(inject([AdminBatchService], (batchService: AdminBatchService) => {
      spyOn(batchService, 'getBatchJobInstances').and.returnValue(of(batchJobInstances));

      fixture.detectChanges();
    }));

    it('should load batch jobs server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
      expectBatchJobsDatatable();
      expectInitialCall(batchService);
    }));

    describe('pagination', () => {

      it('should paginate batch jobs server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
        component.page.limit = 1; // to simplify paging with a small amount of test data

        fixture.detectChanges();

        const pager = fixture.debugElement.query(By.css('.pager'));
        const pagerButtons = pager.queryAll(By.css('li'));
        expect(pagerButtons.length).toBe(7);
        const nextPageButton = pagerButtons[5];
        const nextPageLink = nextPageButton.query(By.css('a'));
        nextPageLink.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances).toHaveBeenCalled(); // initial call
        expect(batchService.getBatchJobInstances)
          .toHaveBeenCalledWith({ page: 1, size: 1, sort: ['jobExecution.startTime,desc'] });
      }));

    });

    describe('sorting', () => {

      it('should sort batch jobs by id server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
        expectInitialCall(batchService);

        const headers = fixture.debugElement.queryAll(By.css('.datatable-header-cell'));
        const idHeader = headers[headersIndexes.id];
        const idHeaderLabel = idHeader.query(By.css('.datatable-header-cell-label'));
        idHeaderLabel.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances)
          .toHaveBeenCalledWith({ page: 0, size: component.page.limit, sort: ['jobExecution.startTime,desc', 'id,asc'] });
      }));

      it('should sort batch jobs by name server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
        expectInitialCall(batchService);

        const headers = fixture.debugElement.queryAll(By.css('.datatable-header-cell'));
        const nameHeader = headers[headersIndexes.name];
        const nameHeaderLabel = nameHeader.query(By.css('.datatable-header-cell-label'));
        nameHeaderLabel.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances)
          .toHaveBeenCalledWith({ page: 0, size: component.page.limit, sort: ['jobExecution.startTime,desc', 'name,asc'] });
      }));

      it('should sort batch jobs by status server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
        expectInitialCall(batchService);

        const headers = fixture.debugElement.queryAll(By.css('.datatable-header-cell'));
        const statusHeader = headers[headersIndexes.status];
        const statusHeaderLabel = statusHeader.query(By.css('.datatable-header-cell-label'));
        statusHeaderLabel.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances)
          .toHaveBeenCalledWith({ page: 0, size: component.page.limit, sort: ['jobExecution.startTime,desc', 'jobExecution.status,asc'] });
      }));

      it('should remove sort batch jobs by start time server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
        expectInitialCall(batchService);

        const headers = fixture.debugElement.queryAll(By.css('.datatable-header-cell'));
        const startTimeHeader = headers[headersIndexes.startTime];
        const startTimeHeaderLabel = startTimeHeader.query(By.css('.datatable-header-cell-label'));
        startTimeHeaderLabel.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances)
          .toHaveBeenCalledWith({ page: 0, size: component.page.limit, sort: [] });
      }));

      it('should sort batch jobs by end time server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
        expectInitialCall(batchService);

        const headers = fixture.debugElement.queryAll(By.css('.datatable-header-cell'));
        const endTimeHeader = headers[headersIndexes.endTime];
        const endTimeHeaderLabel = endTimeHeader.query(By.css('.datatable-header-cell-label'));
        endTimeHeaderLabel.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances)
          .toHaveBeenCalledWith({ page: 0, size: component.page.limit, sort: ['jobExecution.startTime,desc', 'jobExecution.endTime,asc'] });
      }));

      it('should not sort batch jobs by completion time server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
        expectInitialCall(batchService);

        const headers = fixture.debugElement.queryAll(By.css('.datatable-header-cell'));
        const endTimeHeader = headers[headersIndexes.completionTime];
        const endTimeHeaderLabel = endTimeHeader.query(By.css('.datatable-header-cell-label'));
        endTimeHeaderLabel.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances).toHaveBeenCalledTimes(1);
      }));

      it('should refresh batch jobs server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
        expectInitialCall(batchService);

        const refreshDatatableBtn = fixture.debugElement.query(By.css('#refresh-failed-jobs-btn'));
        refreshDatatableBtn.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances)
          .toHaveBeenCalledWith({ page: component.page.offset, size: component.page.limit, sort: ['jobExecution.startTime,desc'] });
      }));

    });

    describe('batch step executions row detail', () => {

      it('should show row detail of a batch job instance with its step executions',
        inject([AdminBatchService], (batchService: AdminBatchService) => {
        const batchStepExecutions: BatchStepExecution[] = [
          {
            id: 1,
            version: 2,
            name: 'versionsRetrievalJobStep',
            startTime: '2018-08-20T22:20:47.045',
            endTime: null,
            status: 'STARTED',
            commitCount: 0,
            readCount: 0,
            filterCount: 0,
            writeCount: 0,
            readSkipCount: 0,
            writeSkipCount: 0,
            processSkipCount: 0,
            rollbackCount: 0,
            exitCode: 'STARTED',
            exitMessage: '',
            lastUpdated: '2018-08-20T22:20:53.718'
          },
          {
            id: 2,
            version: 2,
            name: 'summonerSpellsRetrievalJobStep',
            startTime: '2018-08-20T22:20:47.054',
            endTime: '2018-08-20T22:20:56.617',
            status: 'FAILED',
            commitCount: 0,
            readCount: 0,
            filterCount: 0,
            writeCount: 0,
            readSkipCount: 0,
            writeSkipCount: 0,
            processSkipCount: 0,
            rollbackCount: 0,
            exitCode: 'FAILED',
            exitMessage: '',
            lastUpdated: '2018-08-20T22:20:56.617'
          },
          {
            id: 5,
            version: 2,
            name: 'championsRetrievalJobStep',
            startTime: '2018-08-20T22:20:47.054',
            endTime: '2018-08-20T22:24:54.346',
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
            lastUpdated: '2018-08-20T22:24:54.346'
          }
        ];
        spyOn(batchService, 'getStepExecutions').and.returnValue(of(batchStepExecutions));

        const championsRetrievalJob = batchJobInstances.content.find(jobInstance => jobInstance.name === 'championsRetrievalJob');

        const championsRetrievalJobRow = getChampionsRetrievalJobRow();
        const championsRetrievalJobColumns = getChampionsRetrievalJobColumns(championsRetrievalJobRow);
        const championsRetrievalJobRowDetailColumn = championsRetrievalJobColumns[headersIndexes.rowDetail];
        championsRetrievalJobRowDetailColumn.query(By.css('button')).triggerEventHandler('click', null);

        fixture.detectChanges();

        const championsRetrievalStepExecutions = fixture.debugElement.query(By.css('.datatable-row-detail'));
        expect(championsRetrievalStepExecutions).toBeTruthy();

        const batchStepExecutionsTable = championsRetrievalStepExecutions.query(By.css('.table'));
        expect(batchStepExecutionsTable).toBeTruthy();

        expect(batchService.getStepExecutions).toHaveBeenCalledWith(championsRetrievalJob.id);
      }));

    });

    function expectBatchJobsDatatable() {
      expect(fixture.debugElement.query(By.css('#batch-jobs-datatable'))).toBeTruthy();
      expect(fixture.debugElement.queryAll(By.css('.datatable-header-cell')).length).toBe(Object.keys(headersIndexes).length);
      const rows = fixture.debugElement.queryAll(By.css('.datatable-body-row'));
      expect(rows.length).toBe(batchJobInstances.content.length);

      // championsRetrievalJob - STARTED row
      const batchJobRow0 = getChampionsRetrievalJobRow();
      const batchJob0Columns = getChampionsRetrievalJobColumns(batchJobRow0);
      const batchJob0RowDetailColumn = batchJob0Columns[headersIndexes.rowDetail];
      expect(batchJob0RowDetailColumn.query(By.css('button')).nativeElement.disabled).toBeFalsy();
      const batchJob0StatusColumn = batchJob0Columns[headersIndexes.status];
      expect(batchJob0StatusColumn.nativeElement.classList).toContain('has-text-warning');
      const batchJob0CompletionColumn = batchJob0Columns[headersIndexes.completionTime];
      expect(batchJob0CompletionColumn.nativeElement.textContent.trim()).toBe('');

      // versionsRetrievalJob - FAILED row
      const batchJobRow1 = getVersionsRetrievalJobRow();
      const batchJob1Columns = getVersionsRetrievalJobColumns(batchJobRow1);
      const batchJob1RowDetailColumn = batchJob1Columns[headersIndexes.rowDetail];
      expect(batchJob1RowDetailColumn.query(By.css('button')).nativeElement.disabled).toBeFalsy();
      const batchJob1StatusColumn = batchJob1Columns[headersIndexes.status];
      expect(batchJob1StatusColumn.nativeElement.classList).toContain('has-text-danger');
      const batchJob1CompletionColumn = batchJob1Columns[headersIndexes.completionTime];
      expect(batchJob1CompletionColumn.nativeElement.textContent.trim()).toBe('0.11 min');

      // allRetrievalsJob - COMPLETED row
      const batchJobRow2 = getAllRetrievalsJobRow();
      const batchJob2Columns = getAllRetrievalsJobColumns(batchJobRow2);
      const batchJob2RowDetailColumn = batchJob2Columns[headersIndexes.rowDetail];
      expect(batchJob2RowDetailColumn.query(By.css('button')).nativeElement.disabled).toBeTruthy();
      const batchJob2StatusColumn = batchJob2Columns[headersIndexes.status];
      expect(batchJob2StatusColumn.nativeElement.classList).toContain('has-text-success');
      const batchJob2CompletionColumn = batchJob2Columns[headersIndexes.completionTime];
      expect(batchJob2CompletionColumn.nativeElement.textContent.trim()).toBe('4.12 min');
    }

    function getChampionsRetrievalJobRow(): DebugElement {
      const rows = fixture.debugElement.queryAll(By.css('.datatable-body-row'));
      return rows[batchJobInstances.content.findIndex(jobInstance => jobInstance.name === 'championsRetrievalJob')]
        .query(By.css('.datatable-row-center'));
    }

    function getChampionsRetrievalJobColumns(championsRetrievalJobRow): DebugElement[] {
      return championsRetrievalJobRow.queryAll(By.css('.datatable-body-cell'));
    }

    function getVersionsRetrievalJobRow(): DebugElement {
      const rows = fixture.debugElement.queryAll(By.css('.datatable-body-row'));
      return rows[batchJobInstances.content.findIndex(jobInstance => jobInstance.name === 'versionsRetrievalJob')]
        .query(By.css('.datatable-row-center'));
    }

    function getVersionsRetrievalJobColumns(versionsRetrievalJobRow): DebugElement[] {
      return versionsRetrievalJobRow.queryAll(By.css('.datatable-body-cell'));
    }

    function getAllRetrievalsJobRow(): DebugElement {
      const rows = fixture.debugElement.queryAll(By.css('.datatable-body-row'));
      return rows[batchJobInstances.content.findIndex(jobInstance => jobInstance.name === 'allRetrievalsJob')]
        .query(By.css('.datatable-row-center'));
    }

    function getAllRetrievalsJobColumns(allRetrievalsJobRow): DebugElement[] {
      return allRetrievalsJobRow.queryAll(By.css('.datatable-body-cell'));
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

    beforeEach(inject([AdminBatchService], (batchService: AdminBatchService) => {
      spyOn(batchService, 'getBatchJobInstances').and.returnValue(of(batchJobInstances));
      spyOn(batchService, 'hasFailedAllRetrievalsJob').and.returnValue(of(true));
      spyOn(batchService, 'restartAllRetrievalsJob').and.returnValue(of(batchJobExecution));

      fixture.detectChanges();
    }));

    it('should show the recent failed all retrievals job alert', inject([AdminBatchService], (batchService: AdminBatchService) => {
      expect(fixture.debugElement.query(By.css('#failed-job-alert'))).toBeTruthy();

      const restartJobBtn = fixture.debugElement.query(By.css('#restart-job-btn'));
      expect(restartJobBtn.nativeElement.textContent.trim()).toBe('Restart');
      restartJobBtn.triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(batchService.restartAllRetrievalsJob).toHaveBeenCalled();
      expect(batchService.hasFailedAllRetrievalsJob).toHaveBeenCalledTimes(2);
      expect(batchService.hasFailedAllRetrievalsJob).toHaveBeenCalledWith(component.minutesAgo);
      expect(batchService.getBatchJobInstances)
        .toHaveBeenCalledWith({ page: component.page.offset, size: component.page.limit, sort: ['jobExecution.startTime,desc'] });
    }));

  });

  function expectInitialCall(batchService: AdminBatchService) {
    expect(batchService.getBatchJobInstances)
      .toHaveBeenCalledWith({ page: component.page.offset, size: component.page.limit, sort: ['jobExecution.startTime,desc'] });
  }

});
