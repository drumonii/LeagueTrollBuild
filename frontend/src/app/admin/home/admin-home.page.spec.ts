import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { AdminHomeModule } from './admin-home.module';
import { AdminHomePage } from './admin-home.page';

describe('AdminHomePage', () => {
  let component: AdminHomePage;
  let fixture: ComponentFixture<AdminHomePage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdminHomeModule, HttpClientTestingModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminHomePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(fixture.debugElement.query(By.css('#cpu-usage-card'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('#failed-jobs-card'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('#memory-usage-card'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('#servlet-errors-card'))).toBeTruthy();
  });

});
