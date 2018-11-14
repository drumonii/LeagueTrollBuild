import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CpuUsageComponent } from './cpu-usage.component';

describe('CpuUsageComponent', () => {
  let component: CpuUsageComponent;
  let fixture: ComponentFixture<CpuUsageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CpuUsageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CpuUsageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
