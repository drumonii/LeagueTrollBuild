import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';

import { AdminHeaderComponent } from './admin-header.component';

describe('AdminHeaderComponent', () => {
  let component: AdminHeaderComponent;
  let fixture: ComponentFixture<AdminHeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [AdminHeaderComponent]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminHeaderComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should show admin header', () => {
    expect(fixture.debugElement.query(By.css('#admin-header'))).toBeTruthy();
  });

  it('should collapse navbar on burger click', () => {
    const adminNav = fixture.debugElement.query(By.css('#admin-nav'));
    expect(adminNav.classes['is-active']).toBeFalsy();

    const burgerCollapse = fixture.debugElement.query(By.css('#burger-collapse'));
    expect(burgerCollapse.classes['is-active']).toBeFalsy();

    burgerCollapse.triggerEventHandler('click', null);

    fixture.detectChanges();

    expect(adminNav.classes['is-active']).toBeTruthy();
    expect(burgerCollapse.classes['is-active']).toBeTruthy();
  });

  describe('with authenticated admin', () => {

    xit('should show navbar end dropdown with logout', () => {

    });

  });

  describe('with unauthenticated admin', () => {

    xit('should hide navbar end dropdown with logout', () => {

    });

  });

});
