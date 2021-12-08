import { ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { FlywayModule } from './flyway.module';
import { FlywayPage } from './flyway.page';
import { FlywayService } from './flyway.service';
import { FlywayMigration } from './flyway-response';
import { AdminTitleService } from '@admin-service/admin-title.service';

describe('FlywayPage', () => {
  let component: FlywayPage;
  let fixture: ComponentFixture<FlywayPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NoopAnimationsModule, FlywayModule]
    })
    .compileComponents();
  });

  beforeEach(inject([AdminTitleService], (title: AdminTitleService) => {
    fixture = TestBed.createComponent(FlywayPage);
    component = fixture.componentInstance;

    spyOn(title, 'setTitle').and.callThrough();
  }));

  afterEach(inject([AdminTitleService], (title: AdminTitleService) => {
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

    const tableHeaders = [
      { title: 'Description' },
      { title: 'Script' },
      { title: 'State' },
      { title: 'Installed On' },
      { title: 'Execution Time' }
    ];

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

      const refreshDatatableBtn = fixture.debugElement.query(By.css('[data-e2e="refresh-flyway-btn"]'));
      refreshDatatableBtn.triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(flywayService.getFlyway).toHaveBeenCalledTimes(2);
    }));

    function expectFlywayDatatable() {
      expect(fixture.debugElement.query(By.css('[data-e2e="flyway-datatable"]'))).toBeTruthy();
      const dataTableHeaders = fixture.debugElement.queryAll(By.css('clr-dg-column'));
      expect(dataTableHeaders.length).toBe(tableHeaders.length);

      for (let i = 0; i < dataTableHeaders.length; i++) {
        const header = dataTableHeaders[i];
        const headerLabel = header.query(By.css('button.datagrid-column-title'));
        expect(headerLabel.nativeElement.textContent.trim()).toBe(tableHeaders[i].title);
      }

      const rows = fixture.debugElement.queryAll(By.css('clr-dg-row'));
      expect(rows.length).toBe(flywayMigrations.length);
    }

  });

});
