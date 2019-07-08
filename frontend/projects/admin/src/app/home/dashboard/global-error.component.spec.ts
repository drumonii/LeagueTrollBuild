import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { GlobalErrorModule } from './global-error.module';
import { GlobalErrorComponent } from './global-error.component';
import { GlobalErrorService } from './global-error.service';

describe('GlobalErrorComponent', () => {
  let component: GlobalErrorComponent;
  let fixture: ComponentFixture<GlobalErrorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, GlobalErrorModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GlobalErrorComponent);
    component = fixture.componentInstance;
  });

  describe('loaded global errors', () => {

    afterEach(() => {
      expectNotRefreshing();
    });

    describe('with 0 global errors', () => {

      const servletErrorsCount = 0;

      beforeEach(inject([GlobalErrorService], (service: GlobalErrorService) => {
        spyOn(service, 'getGlobalErrors').and.returnValue(of(servletErrorsCount));

        fixture.detectChanges();
      }));

      it('should show global errors', () => {
        const servletErrors = fixture.debugElement.query(By.css('#global-errors'));
        expect(servletErrors.nativeElement.textContent.trim()).toBe(`${servletErrorsCount}`);
        expect(servletErrors.nativeElement.classList).toContain('label');
      });

    });

    describe('with > 0 global errors', () => {

      const servletErrorsCount = 1;

      beforeEach(inject([GlobalErrorService], (service: GlobalErrorService) => {
        spyOn(service, 'getGlobalErrors').and.returnValue(of(servletErrorsCount));

        fixture.detectChanges();
      }));

      it('should show global errors', () => {
        const servletErrors = fixture.debugElement.query(By.css('#global-errors'));
        expect(servletErrors.nativeElement.textContent.trim()).toBe(`${servletErrorsCount}`);
        expect(servletErrors.nativeElement.classList).toContain('label-danger');

        expectNotRefreshing();
      });

    });

    function expectNotRefreshing() {
      const refreshServletErrorsBtn = fixture.debugElement.query(By.css('#refresh-global-errors-btn'));
      expect(refreshServletErrorsBtn.nativeElement.disabled).toBe(false);
    }

  });

  describe('loading global errors', () => {

    beforeEach(inject([GlobalErrorService], (service: GlobalErrorService) => {
      spyOn(service, 'getGlobalErrors').and.callThrough();

      fixture.detectChanges();
    }));

    it('should show loading indicator', () => {
      expect(fixture.debugElement.query(By.css('#loading-global-errors-alert'))).toBeTruthy();

      const refreshServletErrorsBtn = fixture.debugElement.query(By.css('#refresh-global-errors-btn'));
      expect(refreshServletErrorsBtn.nativeElement.disabled).toBe(true);
    });

  });

  describe('error loading global errors', () => {

    beforeEach(inject([GlobalErrorService], (service: GlobalErrorService) => {
      spyOn(service, 'getGlobalErrors').and.returnValue(of(null));

      fixture.detectChanges();
    }));

    it('should show error alert', () => {
      expect(fixture.debugElement.query(By.css('#no-global-errors-alert'))).toBeTruthy();
    });

  });

});
