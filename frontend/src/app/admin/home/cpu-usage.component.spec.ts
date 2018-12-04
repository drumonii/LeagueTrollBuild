import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { CpuUsageComponent } from './cpu-usage.component';
import { CpuUsageModule } from './cpu-usage.module';
import { CpuUsageService } from './cpu-usage.service';

describe('CpuUsageComponent', () => {
  let component: CpuUsageComponent;
  let fixture: ComponentFixture<CpuUsageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CpuUsageModule, HttpClientTestingModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CpuUsageComponent);
    component = fixture.componentInstance;
  });

  describe('loaded cpu usage', () => {

    beforeEach(inject([CpuUsageService], (service: CpuUsageService) => {
      spyOn(service, 'getCpuUsagePerc').and.returnValue(of('46.172'));
      spyOn(service, 'getCpuCount').and.returnValue(of(2.0));

      fixture.detectChanges();
    }));

    it('should show cpu usage', () => {
      const cpuUsage = fixture.debugElement.query(By.css('#cpu-usage'));
      expect(cpuUsage.nativeElement.textContent.trim()).toBe('46.172% of 2 CPUs');

      const refreshServletErrorsBtn = fixture.debugElement.query(By.css('#refresh-cpu-usage-btn'));
      expect(refreshServletErrorsBtn.query(By.css('i')).classes['fa-spin']).toBeFalsy();
    });

  });

  describe('loading cpu usage', () => {

    beforeEach(inject([CpuUsageService], (service: CpuUsageService) => {
      spyOn(service, 'getCpuUsagePerc').and.callThrough();
      spyOn(service, 'getCpuCount').and.callThrough();

      fixture.detectChanges();
    }));

    it('should show loading indicator', () => {
      expect(fixture.debugElement.query(By.css('#loading-cpu-usage'))).toBeTruthy();

      const refreshServletErrorsBtn = fixture.debugElement.query(By.css('#refresh-cpu-usage-btn'));
      expect(refreshServletErrorsBtn.query(By.css('i')).classes['fa-spin']).toBeTruthy();
    });

  });

  describe('error loading cpu usage', () => {

    describe('error loading cpu percentage', () => {

      beforeEach(inject([CpuUsageService], (service: CpuUsageService) => {
        spyOn(service, 'getCpuUsagePerc').and.returnValue(of(null));
        spyOn(service, 'getCpuCount').and.returnValue(of(4.0));

        fixture.detectChanges();
      }));

      it('should show error alert', () => {
        expect(fixture.debugElement.query(By.css('#no-cpu-usage-alert'))).toBeTruthy();
      });

    });

    describe('error loading cpu count', () => {

      beforeEach(inject([CpuUsageService], (service: CpuUsageService) => {
        spyOn(service, 'getCpuUsagePerc').and.returnValue(of('10.558'));
        spyOn(service, 'getCpuCount').and.returnValue(of(null));

        fixture.detectChanges();
      }));

      it('should show error alert', () => {
        expect(fixture.debugElement.query(By.css('#no-cpu-usage-alert'))).toBeTruthy();
      });

    });

  });

});
