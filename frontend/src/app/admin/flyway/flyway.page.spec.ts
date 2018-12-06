import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { FlywayModule } from './flyway.module';
import { FlywayPage } from './flyway.page';
import { FlywayService } from './flyway.service';
import { FlywayMigration } from './flyway-response';
import { TitleService } from '@service/title.service';

describe('FlywayPage', () => {
  let component: FlywayPage;
  let fixture: ComponentFixture<FlywayPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [FlywayModule, HttpClientTestingModule]
    })
    .compileComponents();
  }));

  beforeEach(inject([TitleService], (title: TitleService) => {
    fixture = TestBed.createComponent(FlywayPage);
    component = fixture.componentInstance;

    spyOn(title, 'setTitle').and.callThrough();
  }));

  afterEach(inject([TitleService], (title: TitleService) => {
    expect(title.setTitle).toHaveBeenCalledWith('Flyway Migrations');
  }));

  describe('flyway migrations datatable', () => {

    const flywayMigrations: FlywayMigration[] = [
      {
        type: 'SQL',
        checksum: -1349535322,
        version: '1440823859',
        description: 'USERS',
        script: 'V1440823859__USERS.sql',
        state: 'SUCCESS',
        installedBy: 'postgres',
        installedOn: '2018-08-21T02:18:29.183Z',
        installedRank: 26,
        executionTime: 5
      }
    ];

    const headersIndexes = { description: 0, script: 1, state: 2, installedOn: 3, executionTime: 4 };

    beforeEach(inject([FlywayService], (flywayService: FlywayService) => {
      spyOn(flywayService, 'getFlyway').and.returnValue(of(flywayMigrations));

      fixture.detectChanges();
    }));

    it('should show flyway migrations datatable', inject([FlywayService], (flywayService: FlywayService) => {
      expectFlywayDatatable();

      expect(flywayService.getFlyway).toHaveBeenCalled();
    }));

    it('should refresh flyway migrations', inject([FlywayService], (flywayService: FlywayService) => {
      expectFlywayDatatable();

      const refreshDatatableBtn = fixture.debugElement.query(By.css('#refresh-failed-jobs-btn'));
      refreshDatatableBtn.triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(flywayService.getFlyway).toHaveBeenCalledTimes(2);
    }));

    function expectFlywayDatatable() {
      expect(fixture.debugElement.query(By.css('#flyway-datatable'))).toBeTruthy();
      const headers = fixture.debugElement.queryAll(By.css('.datatable-header-cell'));
      expect(headers.length).toBe(Object.keys(headersIndexes).length);

      const descriptionHeader = headers[headersIndexes.description];
      const descriptionHeaderLabel = descriptionHeader.query(By.css('.datatable-header-cell-label'));
      expect(descriptionHeaderLabel.nativeElement.textContent.trim()).toBe('Description');

      const scriptHeader = headers[headersIndexes.script];
      const scriptLabel = scriptHeader.query(By.css('.datatable-header-cell-label'));
      expect(scriptLabel.nativeElement.textContent.trim()).toBe('Script');

      const stateHeader = headers[headersIndexes.state];
      const stateLabel = stateHeader.query(By.css('.datatable-header-cell-label'));
      expect(stateLabel.nativeElement.textContent.trim()).toBe('State');

      const installedOnHeader = headers[headersIndexes.installedOn];
      const installedOnLabel = installedOnHeader.query(By.css('.datatable-header-cell-label'));
      expect(installedOnLabel.nativeElement.textContent.trim()).toBe('Installed On');

      const executionTimeHeader = headers[headersIndexes.executionTime];
      const executionTimeLabel = executionTimeHeader.query(By.css('.datatable-header-cell-label'));
      expect(executionTimeLabel.nativeElement.textContent.trim()).toBe('Execution Time');

      const rows = fixture.debugElement.queryAll(By.css('.datatable-body-row'));
      expect(rows.length).toBe(flywayMigrations.length);
    }

  });

});
