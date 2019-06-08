import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { FailedJobsComponent } from './failed-jobs.component';
import { FailedJobsModule } from './failed-jobs.module';
import { FailedJobsService } from './failed-jobs.service';

describe('FailedJobsComponent', () => {
  let component: FailedJobsComponent;
  let fixture: ComponentFixture<FailedJobsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [FailedJobsModule, HttpClientTestingModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FailedJobsComponent);
    component = fixture.componentInstance;
  });

  describe('loaded failed jobs', () => {

    afterEach(() => {
      expectNotRefreshing();
    });

    describe('with 0 failed jobs', () => {

      const failedJobsCount = 0;

      beforeEach(inject([FailedJobsService], (service: FailedJobsService) => {
        spyOn(service, 'getFailedJobs').and.returnValue(of(failedJobsCount));

        fixture.detectChanges();
      }));

      it('should show failed jobs', () => {
        const failedJobs = fixture.debugElement.query(By.css('#failed-jobs'));
        expect(failedJobs.nativeElement.textContent.trim()).toBe(`${failedJobsCount}`);
        expect(failedJobs.nativeElement.classList).toContain('has-text-black');
      });

    });

    describe('with > 0 failed jobs', () => {

      const failedJobsCount = 1;

      beforeEach(inject([FailedJobsService], (service: FailedJobsService) => {
        spyOn(service, 'getFailedJobs').and.returnValue(of(failedJobsCount));

        fixture.detectChanges();
      }));

      it('should show failed jobs', () => {
        const failedJobs = fixture.debugElement.query(By.css('#failed-jobs'));
        expect(failedJobs.nativeElement.textContent.trim()).toBe(`${failedJobsCount}`);
        expect(failedJobs.nativeElement.classList).toContain('has-text-danger');
      });

    });

    function expectNotRefreshing() {
      const refreshFailedJobsBtn = fixture.debugElement.query(By.css('#refresh-failed-jobs-btn'));
      expect(refreshFailedJobsBtn.query(By.css('i')).classes['fa-spin']).toBeFalsy();
    }

  });

  describe('loading failed jobs', () => {

    beforeEach(inject([FailedJobsService], (service: FailedJobsService) => {
      spyOn(service, 'getFailedJobs').and.callThrough();

      fixture.detectChanges();
    }));

    it('should show loading indicator', () => {
      expect(fixture.debugElement.query(By.css('#loading-failed-jobs-alert'))).toBeTruthy();

      const refreshFailedJobsBtn = fixture.debugElement.query(By.css('#refresh-failed-jobs-btn'));
      expect(refreshFailedJobsBtn.query(By.css('i')).classes['fa-spin']).toBeTruthy();
    });

  });

  describe('error loading failed jobs', () => {

    beforeEach(inject([FailedJobsService], (service: FailedJobsService) => {
      spyOn(service, 'getFailedJobs').and.returnValue(of(null));

      fixture.detectChanges();
    }));

    it('should show error alert', () => {
      expect(fixture.debugElement.query(By.css('#no-failed-jobs-alert'))).toBeTruthy();
    });

  });

});
