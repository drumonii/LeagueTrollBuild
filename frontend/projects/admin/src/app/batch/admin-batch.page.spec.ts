import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { AdminBatchModule } from './admin-batch.module';
import { AdminBatchPage } from './admin-batch.page';
import { BatchJobDatagridComponent } from './batch-job-datagrid.component';
import { AdminTitleService } from '@admin-service/admin-title.service';

describe('AdminBatchPage', () => {
  let component: AdminBatchPage;
  let fixture: ComponentFixture<AdminBatchPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AdminBatchModule]
    })
    .compileComponents();
  }));

  beforeEach(inject([AdminTitleService], (title: AdminTitleService) => {
    fixture = TestBed.createComponent(AdminBatchPage);
    component = fixture.componentInstance;

    spyOn(title, 'setTitle').and.callThrough();

    fixture.detectChanges();
  }));

  afterEach(inject([AdminTitleService], (title: AdminTitleService) => {
    expect(title.setTitle).toHaveBeenCalledWith('Batch Jobs');
  }));

  it('should show the batch page', () => {
    expect(fixture.debugElement.query(By.directive(BatchJobDatagridComponent))).toBeTruthy();
  });

});
