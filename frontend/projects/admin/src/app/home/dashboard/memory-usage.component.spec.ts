import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { MemoryUsageComponent } from './memory-usage.component';
import { MemoryUsageModule } from './memory-usage.module';
import { MemoryUsageService } from './memory-usage.service';

describe('MemoryUsageComponent', () => {
  let component: MemoryUsageComponent;
  let fixture: ComponentFixture<MemoryUsageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MemoryUsageModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MemoryUsageComponent);
    component = fixture.componentInstance;
  });

  describe('loaded memory usage', () => {

    afterEach(() => {
      expectNotRefreshing();
    });

    describe('with >= 512MB memory usage', () => {

      const mbInUseUsage = '512';

      beforeEach(inject([MemoryUsageService], (service: MemoryUsageService) => {
        spyOn(service, 'getMemoryUsage').and.returnValue(of(mbInUseUsage));

        fixture.detectChanges();
      }));

      it('should show memory usage', () => {
        const memoryUsage = fixture.debugElement.query(By.css('#memory-usage'));
        expect(memoryUsage.nativeElement.textContent.trim()).toBe(`${mbInUseUsage} MB`);
        expect(memoryUsage.nativeElement.classList).toContain('label-danger');
      });

    });

    describe('with >= 400MB memory usage', () => {

      const mbInUseUsage = '400';

      beforeEach(inject([MemoryUsageService], (service: MemoryUsageService) => {
        spyOn(service, 'getMemoryUsage').and.returnValue(of(mbInUseUsage));

        fixture.detectChanges();
      }));

      it('should show memory usage', () => {
        const memoryUsage = fixture.debugElement.query(By.css('#memory-usage'));
        expect(memoryUsage.nativeElement.textContent.trim()).toBe(`${mbInUseUsage} MB`);
        expect(memoryUsage.nativeElement.classList).toContain('label-warning');
      });

    });

    describe('with < 400MB memory usage', () => {

      const mbInUseUsage = '325';

      beforeEach(inject([MemoryUsageService], (service: MemoryUsageService) => {
        spyOn(service, 'getMemoryUsage').and.returnValue(of(mbInUseUsage));

        fixture.detectChanges();
      }));

      it('should show memory usage', () => {
        const memoryUsage = fixture.debugElement.query(By.css('#memory-usage'));
        expect(memoryUsage.nativeElement.textContent.trim()).toBe(`${mbInUseUsage} MB`);
        expect(memoryUsage.nativeElement.classList).toContain('label');
      });

    });

    function expectNotRefreshing() {
      const refreshMemoryUsageBtn = fixture.debugElement.query(By.css('#refresh-memory-usage-btn'));
      expect(refreshMemoryUsageBtn.nativeElement.disabled).toBe(false);
    }

  });

  describe('loading memory usage', () => {

    beforeEach(inject([MemoryUsageService], (service: MemoryUsageService) => {
      spyOn(service, 'getMemoryUsage').and.callThrough();

      fixture.detectChanges();
    }));

    it('should show loading indicator', () => {
      expect(fixture.debugElement.query(By.css('#loading-memory-usage-alert'))).toBeTruthy();

      const refreshMemoryUsageBtn = fixture.debugElement.query(By.css('#refresh-memory-usage-btn'));
      expect(refreshMemoryUsageBtn.nativeElement.disabled).toBe(true);
    });

  });

  describe('error loading memory usage', () => {

    beforeEach(inject([MemoryUsageService], (service: MemoryUsageService) => {
      spyOn(service, 'getMemoryUsage').and.returnValue(of(null));

      fixture.detectChanges();
    }));

    it('should show error alert', () => {
      expect(fixture.debugElement.query(By.css('#no-memory-usage-alert'))).toBeTruthy();
    });

  });

});
