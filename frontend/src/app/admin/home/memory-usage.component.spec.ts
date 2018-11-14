import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MemoryUsageComponent } from './memory-usage.component';

describe('MemoryUsageComponent', () => {
  let component: MemoryUsageComponent;
  let fixture: ComponentFixture<MemoryUsageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MemoryUsageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MemoryUsageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
