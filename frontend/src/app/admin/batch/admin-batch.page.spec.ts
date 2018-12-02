import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { AdminBatchModule } from './admin-batch.module';
import { AdminBatchPage } from './admin-batch.page';
import { AdminBatchService } from './admin-batch.service';
import { TitleService } from '@service/title.service';
import { BatchJobInstance } from '@admin-model/batch-job-instance';
import { Paginated } from '@admin-model/paginated';

describe('AdminBatchPage', () => {
  let component: AdminBatchPage;
  let fixture: ComponentFixture<AdminBatchPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdminBatchModule, HttpClientTestingModule]
    })
    .compileComponents();
  }));

  beforeEach(inject([TitleService], (title: TitleService) => {
    fixture = TestBed.createComponent(AdminBatchPage);
    component = fixture.componentInstance;

    spyOn(title, 'setTitle').and.callThrough();
  }));

  afterEach(inject([TitleService], (title: TitleService) => {
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
            createTime: '2018-08-20T22:20:47.068',
            startTime: '2018-08-20T22:20:47.071',
            endTime: '2018-08-20T22:24:54.344',
            status: 'STARTED',
            exitCode: 'STARTED',
            exitMessage: '',
            lastUpdated: '2018-08-20T22:24:54.344'
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
        const idHeader = headers[0];
        const idHeaderLabel = idHeader.query(By.css('.datatable-header-cell-label'));
        idHeaderLabel.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances)
          .toHaveBeenCalledWith({ page: 0, size: component.page.limit, sort: ['jobExecution.startTime,desc', 'id,asc'] });
      }));

      it('should sort batch jobs by name server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
        expectInitialCall(batchService);

        const headers = fixture.debugElement.queryAll(By.css('.datatable-header-cell'));
        const nameHeader = headers[1];
        const nameHeaderLabel = nameHeader.query(By.css('.datatable-header-cell-label'));
        nameHeaderLabel.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances)
          .toHaveBeenCalledWith({ page: 0, size: component.page.limit, sort: ['jobExecution.startTime,desc', 'name,asc'] });
      }));

      it('should sort batch jobs by status server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
        expectInitialCall(batchService);

        const headers = fixture.debugElement.queryAll(By.css('.datatable-header-cell'));
        const statusHeader = headers[2];
        const statusHeaderLabel = statusHeader.query(By.css('.datatable-header-cell-label'));
        statusHeaderLabel.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances)
          .toHaveBeenCalledWith({ page: 0, size: component.page.limit, sort: ['jobExecution.startTime,desc', 'jobExecution.status,asc'] });
      }));

      it('should remove sort batch jobs by start time server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
        expectInitialCall(batchService);

        const headers = fixture.debugElement.queryAll(By.css('.datatable-header-cell'));
        const startTimeHeader = headers[3];
        const startTimeHeaderLabel = startTimeHeader.query(By.css('.datatable-header-cell-label'));
        startTimeHeaderLabel.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances)
          .toHaveBeenCalledWith({ page: 0, size: component.page.limit, sort: [] });
      }));

      it('should sort batch jobs by end time server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
        expectInitialCall(batchService);

        const headers = fixture.debugElement.queryAll(By.css('.datatable-header-cell'));
        const endTimeHeader = headers[4];
        const endTimeHeaderLabel = endTimeHeader.query(By.css('.datatable-header-cell-label'));
        endTimeHeaderLabel.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(batchService.getBatchJobInstances)
          .toHaveBeenCalledWith({ page: 0, size: component.page.limit, sort: ['jobExecution.startTime,desc', 'jobExecution.endTime,asc'] });
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

    function expectBatchJobsDatatable() {
      expect(fixture.debugElement.query(By.css('#batch-jobs-datatable'))).toBeTruthy();
      expect(fixture.debugElement.queryAll(By.css('.datatable-header-cell')).length).toBe(5);
      const rows = fixture.debugElement.queryAll(By.css('.datatable-body-row'));
      expect(rows.length).toBe(batchJobInstances.content.length);

      // STARTED row
      const startedBatchJobRow = rows[0].query(By.css('.datatable-row-center'));
      const startedBatchJobColumns = startedBatchJobRow.queryAll(By.css('.datatable-body-cell'));
      const startedBatchJobStatusColumn = startedBatchJobColumns[2];
      expect(startedBatchJobStatusColumn.nativeElement.classList.contains('has-text-warning')).toBe(true);

      // FAILED row
      const failedBatchJobRow = rows[1].query(By.css('.datatable-row-center'));
      const failedBatchJobColumns = failedBatchJobRow.queryAll(By.css('.datatable-body-cell'));
      const failedBatchJobStatusColumn = failedBatchJobColumns[2];
      expect(failedBatchJobStatusColumn.nativeElement.classList.contains('has-text-danger')).toBe(true);

      // COMPLETED row
      const completedBatchJobRow = rows[2].query(By.css('.datatable-row-center'));
      const completedBatchJobColumns = completedBatchJobRow.queryAll(By.css('.datatable-body-cell'));
      const completedBatchJobStatusColumn = completedBatchJobColumns[2];
      expect(completedBatchJobStatusColumn.nativeElement.classList.contains('has-text-success')).toBe(true);
    }

    function expectInitialCall(batchService: AdminBatchService) {
      expect(batchService.getBatchJobInstances)
        .toHaveBeenCalledWith({ page: component.page.offset, size: component.page.limit, sort: ['jobExecution.startTime,desc'] });
    }

  });

});
