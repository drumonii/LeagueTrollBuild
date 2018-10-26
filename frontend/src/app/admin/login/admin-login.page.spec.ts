import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminLoginPage } from './admin-login.page';

describe('AdminLoginPage', () => {
  let component: AdminLoginPage;
  let fixture: ComponentFixture<AdminLoginPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminLoginPage ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminLoginPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
