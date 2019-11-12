import { async, ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';
import { delay } from 'rxjs/operators';

import { HttpStatsModule } from './http-stats.module';
import { HttpStatsComponent } from './http-stats.component';
import { HttpStats, HttpStatsService } from './http-stats.service';

describe('HttpStatsComponent', () => {
  let component: HttpStatsComponent;
  let fixture: ComponentFixture<HttpStatsComponent>;

  const cardContentSets = 5;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, HttpStatsModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HttpStatsComponent);
    component = fixture.componentInstance;
  });

  describe('loaded http stats', () => {

    const httpStats: HttpStats = {
      measurements: {
        count: 155,
        max: 0.0026436
      },
      exceptions: ['NullPointerException'],
      uris: ['/api/admin/authentication'],
      statuses: ['500', '400', '200']
    };

    beforeEach(inject([HttpStatsService], (service: HttpStatsService) => {
      spyOn(service, 'getHttpStats').and.returnValue(of(httpStats));
    }));

    afterEach(inject([HttpStatsService], (service: HttpStatsService) => {
      expect(service.getHttpStats).toHaveBeenCalled();
    }));

    it('should show the http statistics', () => {
      fixture.detectChanges();

      expectCardContentSets();

      const httpRequestsCount = fixture.debugElement.query(By.css('[data-e2e="http-requests-count"]'));
      expect(httpRequestsCount.nativeElement.textContent.trim()).toBe(httpStats.measurements.count.toString(10));

      const httpRequestsMax = fixture.debugElement.query(By.css('[data-e2e="http-requests-max-seconds"]'));
      expect(httpRequestsMax.nativeElement.textContent.trim()).toBe(`${httpStats.measurements.max} seconds`);

      const httpExceptions = fixture.debugElement.query(By.css('[data-e2e="http-exceptions"]'));
      expect(httpExceptions.queryAll(By.css('li')).length).toBe(httpStats.exceptions.length);

      const httpUris = fixture.debugElement.query(By.css('[data-e2e="http-uris"]'));
      expect(httpUris.queryAll(By.css('li')).length).toBe(httpStats.uris.length);

      const httpStatuses = fixture.debugElement.query(By.css('[data-e2e="http-statuses"]'));
      expect(httpStatuses.queryAll(By.css('li')).length).toBe(httpStats.statuses.length);

      expect(fixture.debugElement.query(By.css('[data-e2e="http-statuses-has-500-alert"]'))).toBeTruthy();
    });

  });

  describe('loading http stats', () => {

    const networkDelay = 2500;

    beforeEach(inject([HttpStatsService], (service: HttpStatsService) => {
      spyOn(service, 'getHttpStats').and.returnValue(of({
        measurements: null,
        exceptions: null,
        uris: null,
        statuses: null
      }).pipe(delay(networkDelay)));
    }));

    afterEach(inject([HttpStatsService], (service: HttpStatsService) => {
      expect(service.getHttpStats).toHaveBeenCalled();
    }));

    it('should show loading indicator', fakeAsync(() => {
      fixture.detectChanges();

      expect(fixture.debugElement.query(By.css('.progress'))).toBeTruthy();

      expectCardContentSets();

      tick(networkDelay);
    }));

  });

  describe('error loading http stats', () => {

    beforeEach(inject([HttpStatsService], (service: HttpStatsService) => {
      spyOn(service, 'getHttpStats').and.returnValue(of({
        measurements: null,
        exceptions: null,
        uris: null,
        statuses: null
      }));
    }));

    afterEach(inject([HttpStatsService], (service: HttpStatsService) => {
      expect(service.getHttpStats).toHaveBeenCalled();
    }));

    it('should show error alert', () => {
      fixture.detectChanges();

      expect(fixture.debugElement.query(By.css('[data-e2e="error-http-stats-alert"]'))).toBeTruthy();
    });

  });

  function expectCardContentSets() {
    expect(fixture.debugElement.queryAll(By.css('.card-title')).length).toBe(cardContentSets);
    expect(fixture.debugElement.queryAll(By.css('.card-text')).length).toBe(cardContentSets);
  }
});
