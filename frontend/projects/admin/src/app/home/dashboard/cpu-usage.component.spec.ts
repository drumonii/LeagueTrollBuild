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
      imports: [HttpClientTestingModule, CpuUsageModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CpuUsageComponent);
    component = fixture.componentInstance;
  });

  describe('loaded cpu usage', () => {

    beforeEach(inject([CpuUsageService], (service: CpuUsageService) => {
      spyOn(service, 'getCpuCount').and.returnValue(of(2.0));
    }));

    afterEach(() => {
      expectNotRefreshing();
    });

    describe('with high cpu usage', () => {

      beforeEach(inject([CpuUsageService], (service: CpuUsageService) => {
        spyOn(service, 'getCpuUsagePerc').and.returnValue(of('87.734'));

        fixture.detectChanges();
      }));

      it('should show cpu usage', () => {
        const cpuUsage = fixture.debugElement.query(By.css('#cpu-usage'));
        expect(cpuUsage.nativeElement.textContent.trim()).toBe('87.734% of 2 CPUs');
        expect(cpuUsage.nativeElement.classList).toContain('label-danger');

        const cpuUsagePercentage = fixture.debugElement.query(By.css('#cpu-usage-perc'));
        expect(cpuUsagePercentage.nativeElement.classList).toContain('badge');
      });

    });

    describe('with medium cpu usage', () => {

      beforeEach(inject([CpuUsageService], (service: CpuUsageService) => {
        spyOn(service, 'getCpuUsagePerc').and.returnValue(of('35.921'));

        fixture.detectChanges();
      }));

      it('should show cpu usage', () => {
        const cpuUsage = fixture.debugElement.query(By.css('#cpu-usage'));
        expect(cpuUsage.nativeElement.textContent.trim()).toBe('35.921% of 2 CPUs');
        expect(cpuUsage.nativeElement.classList).toContain('label-warning');

        const cpuUsagePercentage = fixture.debugElement.query(By.css('#cpu-usage-perc'));
        expect(cpuUsagePercentage.nativeElement.classList).toContain('badge');
      });

    });

    describe('with low cpu usage', () => {

      beforeEach(inject([CpuUsageService], (service: CpuUsageService) => {
        spyOn(service, 'getCpuUsagePerc').and.returnValue(of('10.012'));

        fixture.detectChanges();
      }));

      it('should show cpu usage', () => {
        const cpuUsage = fixture.debugElement.query(By.css('#cpu-usage'));
        expect(cpuUsage.nativeElement.textContent.trim()).toBe('10.012% of 2 CPUs');
        expect(cpuUsage.nativeElement.classList).toContain('label');

        const cpuUsagePercentage = fixture.debugElement.query(By.css('#cpu-usage-perc'));
        expect(cpuUsagePercentage.nativeElement.classList).toContain('badge');
      });

    });

    function expectNotRefreshing() {
      const refreshServletErrorsBtn = fixture.debugElement.query(By.css('#refresh-cpu-usage-btn'));
      expect(refreshServletErrorsBtn.nativeElement.disabled).toBe(false);
    }

  });

  describe('loading cpu usage', () => {

    beforeEach(inject([CpuUsageService], (service: CpuUsageService) => {
      spyOn(service, 'getCpuUsagePerc').and.callThrough();
      spyOn(service, 'getCpuCount').and.callThrough();

      fixture.detectChanges();
    }));

    it('should show loading indicator', () => {
      expect(fixture.debugElement.query(By.css('#loading-cpu-usage'))).toBeTruthy();

      const refreshCpuUsageBtn = fixture.debugElement.query(By.css('#refresh-cpu-usage-btn'));
      expect(refreshCpuUsageBtn.nativeElement.disabled).toBe(true);
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
