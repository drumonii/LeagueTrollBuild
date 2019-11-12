import { async, ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';
import { delay } from 'rxjs/operators';

import { AppHealthModule } from './app-health.module';
import { AppHealthComponent } from './app-health.component';
import { AppHealth, AppHealthService } from './app-health.service';

describe('AppHealthComponent', () => {
  let component: AppHealthComponent;
  let fixture: ComponentFixture<AppHealthComponent>;

  const cardContentSets = 3;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AppHealthModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppHealthComponent);
    component = fixture.componentInstance;
  });

  describe('loaded app health', () => {

    const scheduledDate = new Date();
    scheduledDate.setHours(scheduledDate.getHours() + 1);

    const appHealth: AppHealth = {
      failedJobs: {
        count: 1
      },
      nextScheduledAllRetrievalsJob: scheduledDate,
      uptime: {
        total: '4 hours, 2 minutes',
        seconds: 14523.682
      }
    };

    beforeEach(inject([AppHealthService], (service: AppHealthService) => {
      spyOn(service, 'getAppHealth').and.returnValue(of(appHealth));
    }));

    afterEach(inject([AppHealthService], (service: AppHealthService) => {
      expect(service.getAppHealth).toHaveBeenCalled();
    }));

    it('should show the app health', () => {
      fixture.detectChanges();

      expectCardContentSets();

      expect(fixture.debugElement.query(By.css('[data-e2e="failed-jobs-alert"]'))).toBeTruthy();
      const failedJobsCount = fixture.debugElement.query(By.css('[data-e2e="failed-jobs-count"]'));
      expect(failedJobsCount.nativeElement.textContent.trim()).toBe(appHealth.failedJobs.count.toString(10));

      const nextScheduledAllRetrievalsJob = fixture.debugElement.query(By.css('[data-e2e="next-scheduled-all-retrievals-job"]'));
      expect(nextScheduledAllRetrievalsJob.nativeElement.textContent.trim()).toContain('from now');

      const uptimeTotal = fixture.debugElement.query(By.css('[data-e2e="uptime-total"]'));
      expect(uptimeTotal.nativeElement.textContent.trim()).toBe(appHealth.uptime.total);
    });

  });

  describe('loading app health', () => {

    const networkDelay = 2500;

    beforeEach(inject([AppHealthService], (service: AppHealthService) => {
      spyOn(service, 'getAppHealth').and.returnValue(of({
        failedJobs: null,
        nextScheduledAllRetrievalsJob: null,
        uptime: null
      }).pipe(delay(networkDelay)));
    }));

    afterEach(inject([AppHealthService], (service: AppHealthService) => {
      expect(service.getAppHealth).toHaveBeenCalled();
    }));

    it('should show loading indicator', fakeAsync(() => {
      fixture.detectChanges();

      expect(fixture.debugElement.query(By.css('.progress'))).toBeTruthy();

      expectCardContentSets();

      tick(networkDelay);
    }));

  });

  describe('error loading app health', () => {

    beforeEach(inject([AppHealthService], (service: AppHealthService) => {
      spyOn(service, 'getAppHealth').and.returnValue(of({
        failedJobs: null,
        nextScheduledAllRetrievalsJob: null,
        uptime: null
      }));
    }));

    afterEach(inject([AppHealthService], (service: AppHealthService) => {
      expect(service.getAppHealth).toHaveBeenCalled();
    }));

    it('should show error alert', () => {
      fixture.detectChanges();

      expect(fixture.debugElement.query(By.css('[data-e2e="error-app-health-alert"]'))).toBeTruthy();
    });

  });

  function expectCardContentSets() {
    expect(fixture.debugElement.queryAll(By.css('.card-title')).length).toBe(cardContentSets);
    expect(fixture.debugElement.queryAll(By.css('.card-text')).length).toBe(cardContentSets);
  }
});
