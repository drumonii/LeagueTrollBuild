import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';

import { AdminHomeModule } from './admin-home.module';
import { AdminHomePage } from './admin-home.page';
import { AdminTitleService } from '@admin-service/admin-title.service';
import { CpuUsageComponent } from './dashboard/cpu-usage.component';
import { FailedJobsComponent } from './dashboard/failed-jobs.component';
import { MemoryUsageComponent } from './dashboard/memory-usage.component';
import { GlobalErrorComponent } from './dashboard/global-error.component';

describe('AdminHomePage', () => {
  let component: AdminHomePage;
  let fixture: ComponentFixture<AdminHomePage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NoopAnimationsModule, AdminHomeModule]
    })
    .compileComponents();
  }));

  beforeEach(inject([AdminTitleService], (title: AdminTitleService) => {
    fixture = TestBed.createComponent(AdminHomePage);
    component = fixture.componentInstance;

    spyOn(title, 'resetTitle').and.callThrough();

    fixture.detectChanges();
  }));

  afterEach(inject([AdminTitleService], (title: AdminTitleService) => {
    expect(title.resetTitle).toHaveBeenCalled();
  }));

  it('should create', () => {
    expect(fixture.debugElement.query(By.directive(CpuUsageComponent))).toBeTruthy();
    expect(fixture.debugElement.query(By.directive(FailedJobsComponent))).toBeTruthy();
    expect(fixture.debugElement.query(By.directive(MemoryUsageComponent))).toBeTruthy();
    expect(fixture.debugElement.query(By.directive(GlobalErrorComponent))).toBeTruthy();
  });

});
