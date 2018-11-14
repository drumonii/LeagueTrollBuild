import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ServletErrorComponent } from './servlet-error.component';

describe('ServletErrorComponent', () => {
  let component: ServletErrorComponent;
  let fixture: ComponentFixture<ServletErrorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ServletErrorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ServletErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
