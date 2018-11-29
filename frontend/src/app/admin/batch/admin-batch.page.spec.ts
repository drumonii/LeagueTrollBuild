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

    component.page.limit = 1; // to simplify paging with a small amount of test data

    spyOn(title, 'setTitle').and.callThrough();
  }));

  afterEach(inject([TitleService], (title: TitleService) => {
    expect(title.setTitle).toHaveBeenCalledWith('Batch Jobs');
  }));

  describe('batch jobs datatable', () => {

    const batchJobInstances: Paginated<BatchJobInstance> = {
      content: [
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
            status: 'COMPLETED',
            exitCode: 'COMPLETED',
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

    beforeEach(inject([AdminBatchService], (batchService: AdminBatchService) => {
      spyOn(batchService, 'getBatchJobInstances').and.returnValue(of(batchJobInstances));

      fixture.detectChanges();
    }));

    afterEach(() => {
      expect(fixture.debugElement.query(By.css('#batch-jobs-datatable'))).toBeTruthy();
      expect(fixture.debugElement.queryAll(By.css('.datatable-header-cell')).length).toBe(4);
    });

    it('should load batch jobs server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
      expectInitialCall(batchService);
    }));

    it('should paginate batch jobs server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
      expectInitialCall(batchService);

      const pager = fixture.debugElement.query(By.css('.pager'));
      const pagerButtons = pager.queryAll(By.css('li'));
      expect(pagerButtons.length).toBe(6);
      const nextPageButton = pagerButtons[4];
      const nextPageLink = nextPageButton.query(By.css('a'));
      nextPageLink.triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(batchService.getBatchJobInstances)
        .toHaveBeenCalledWith({ page: 0, size: component.page.limit, sort: ['jobExecution.startTime,desc'] });
    }));

    it('should sort batch jobs by name server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
      expectInitialCall(batchService);

      const headers = fixture.debugElement.queryAll(By.css('.datatable-header-cell'));
      const nameHeader = headers[0];
      const nameHeaderLabel = nameHeader.query(By.css('.datatable-header-cell-label'));
      nameHeaderLabel.triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(batchService.getBatchJobInstances)
        .toHaveBeenCalledWith({ page: 0, size: component.page.limit, sort: ['jobExecution.startTime,desc', 'name,asc'] });
    }));

    it('should sort batch jobs by status server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
      expectInitialCall(batchService);

      const headers = fixture.debugElement.queryAll(By.css('.datatable-header-cell'));
      const statusHeader = headers[1];
      const statusHeaderLabel = statusHeader.query(By.css('.datatable-header-cell-label'));
      statusHeaderLabel.triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(batchService.getBatchJobInstances)
        .toHaveBeenCalledWith({ page: 0, size: component.page.limit, sort: ['jobExecution.startTime,desc', 'jobExecution.status,asc'] });
    }));

    it('should remove sort batch jobs by start time server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
      expectInitialCall(batchService);

      const headers = fixture.debugElement.queryAll(By.css('.datatable-header-cell'));
      const startTimeHeader = headers[2];
      const startTimeHeaderLabel = startTimeHeader.query(By.css('.datatable-header-cell-label'));
      startTimeHeaderLabel.triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(batchService.getBatchJobInstances)
        .toHaveBeenCalledWith({ page: 0, size: component.page.limit, sort: [] });
    }));

    it('should sort batch jobs by end time server side', inject([AdminBatchService], (batchService: AdminBatchService) => {
      expectInitialCall(batchService);

      const headers = fixture.debugElement.queryAll(By.css('.datatable-header-cell'));
      const endTimeHeader = headers[3];
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

    function expectInitialCall(batchService: AdminBatchService) {
      expect(batchService.getBatchJobInstances)
        .toHaveBeenCalledWith({ page: component.page.offset, size: component.page.limit, sort: ['jobExecution.startTime,desc'] });
    }

  });

});
