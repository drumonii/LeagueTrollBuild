import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { ServletErrorModule } from './servlet-error.module';
import { ServletErrorComponent } from './servlet-error.component';
import { ServletErrorService } from './servlet-error.service';

describe('ServletErrorComponent', () => {
  let component: ServletErrorComponent;
  let fixture: ComponentFixture<ServletErrorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ServletErrorModule, HttpClientTestingModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ServletErrorComponent);
    component = fixture.componentInstance;
  });

  describe('loaded servlet errors', () => {

    afterEach(() => {
      expectNotRefreshing();
    });

    describe('with 0 servlet errors', () => {

      const servletErrorsCount = 0;

      beforeEach(inject([ServletErrorService], (service: ServletErrorService) => {
        spyOn(service, 'getServletErrors').and.returnValue(of(servletErrorsCount));

        fixture.detectChanges();
      }));

      it('should show servlet errors', () => {
        const servletErrors = fixture.debugElement.query(By.css('#servlet-errors'));
        expect(servletErrors.nativeElement.textContent.trim()).toBe(`${servletErrorsCount}`);
        expect(servletErrors.nativeElement.classList).toContain('has-text-black');
      });

    });

    describe('with > 0 servlet errors', () => {

      const servletErrorsCount = 1;

      beforeEach(inject([ServletErrorService], (service: ServletErrorService) => {
        spyOn(service, 'getServletErrors').and.returnValue(of(servletErrorsCount));

        fixture.detectChanges();
      }));

      it('should show servlet errors', () => {
        const servletErrors = fixture.debugElement.query(By.css('#servlet-errors'));
        expect(servletErrors.nativeElement.textContent.trim()).toBe(`${servletErrorsCount}`);
        expect(servletErrors.nativeElement.classList).toContain('has-text-danger');

        expectNotRefreshing();
      });

    });

    function expectNotRefreshing() {
      const refreshServletErrorsBtn = fixture.debugElement.query(By.css('#refresh-servlet-errors-btn'));
      expect(refreshServletErrorsBtn.query(By.css('i')).classes['fa-spin']).toBeFalsy();
    }

  });

  describe('loading servlet errors', () => {

    beforeEach(inject([ServletErrorService], (service: ServletErrorService) => {
      spyOn(service, 'getServletErrors').and.callThrough();

      fixture.detectChanges();
    }));

    it('should show loading indicator', () => {
      expect(fixture.debugElement.query(By.css('#loading-servlet-errors-alert'))).toBeTruthy();

      const refreshServletErrorsBtn = fixture.debugElement.query(By.css('#refresh-servlet-errors-btn'));
      expect(refreshServletErrorsBtn.query(By.css('i')).classes['fa-spin']).toBeTruthy();
    });

  });

  describe('error loading servlet errors', () => {

    beforeEach(inject([ServletErrorService], (service: ServletErrorService) => {
      spyOn(service, 'getServletErrors').and.returnValue(of(null));

      fixture.detectChanges();
    }));

    it('should show error alert', () => {
      expect(fixture.debugElement.query(By.css('#no-servlet-errors-alert'))).toBeTruthy();
    });

  });

});
