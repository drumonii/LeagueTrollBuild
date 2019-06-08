import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { AdminHomeModule } from './admin-home.module';
import { AdminHomePage } from './admin-home.page';
import { AdminTitleService } from '@admin-service/admin-title.service';

describe('AdminHomePage', () => {
  let component: AdminHomePage;
  let fixture: ComponentFixture<AdminHomePage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdminHomeModule, HttpClientTestingModule]
    })
    .compileComponents();
  }));

  beforeEach(inject([AdminTitleService], (title: AdminTitleService) => {
    fixture = TestBed.createComponent(AdminHomePage);
    component = fixture.componentInstance;

    spyOn(title, 'resetTitle').and.callThrough();

    fixture.detectChanges();
  }));

  afterEach(inject([AdminTitleService], (title: AdminTitleService) => {
    expect(title.resetTitle).toHaveBeenCalled();
  }));

  it('should create', () => {
    expect(fixture.debugElement.query(By.css('#cpu-usage-card'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('#failed-jobs-card'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('#memory-usage-card'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('#global-errors-card'))).toBeTruthy();
  });

});
