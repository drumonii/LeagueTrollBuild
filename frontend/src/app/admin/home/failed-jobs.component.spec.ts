import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FailedJobsComponent } from './failed-jobs.component';

describe('FailedJobsComponent', () => {
  let component: FailedJobsComponent;
  let fixture: ComponentFixture<FailedJobsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FailedJobsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FailedJobsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
