import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { AdminAuthService } from '@security/admin-auth.service';
import { AdminUserDetails } from '@security/admin-user-details';
import { AdminHeaderComponent } from './admin-header.component';

describe('AdminHeaderComponent', () => {
  let component: AdminHeaderComponent;
  let fixture: ComponentFixture<AdminHeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      declarations: [AdminHeaderComponent]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminHeaderComponent);
    component = fixture.componentInstance;
  });

  it('should show admin header', () => {
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('#admin-header'))).toBeTruthy();
  });

  describe('with authenticated admin', () => {

    const adminUserDetails: AdminUserDetails = {
      username: 'admin',
      authorities: [
        {
          authority: 'ROLE_ADMIN'
        }
      ]
    };

    beforeEach(inject([AdminAuthService], (authService: AdminAuthService) => {
      spyOnProperty(authService, 'adminUserDetails').and.returnValue(of(adminUserDetails));

      fixture.detectChanges();
    }));

    it('should show navbar end dropdown with logout', () => {
      const authenticatedAdminNavbarEnd = fixture.debugElement.query(By.css('#authenticated-admin-navbar-end'));
      expect(authenticatedAdminNavbarEnd).toBeTruthy();

      const navbarItems = authenticatedAdminNavbarEnd.queryAll(By.css('.navbar-item'));
      expect(navbarItems.length).toBe(3);

      expect(fixture.debugElement.query(By.css('.navbar-dropdown'))).toBeTruthy();
    });

    xit('should logout user on logout button click', () => {
      const adminLogoutBtn = fixture.debugElement.query(By.css('#admin-logout-btn'));
      adminLogoutBtn.triggerEventHandler('click', null);

      fixture.detectChanges();
    });

    it('should collapse navbar on burger click', () => {
      fixture.detectChanges();

      const adminNav = fixture.debugElement.query(By.css('#admin-nav'));
      expect(adminNav.classes['is-active']).toBeFalsy();

      const burgerCollapse = fixture.debugElement.query(By.css('#burger-collapse'));
      expect(burgerCollapse.classes['is-active']).toBeFalsy();

      burgerCollapse.triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(adminNav.classes['is-active']).toBeTruthy();
      expect(burgerCollapse.classes['is-active']).toBeTruthy();
    });

  });

  describe('with unauthenticated admin', () => {

    it('should hide navbar end dropdown with logout', inject([AdminAuthService], (authService: AdminAuthService) => {
      spyOnProperty(authService, 'adminUserDetails').and.returnValue(of(null));

      fixture.detectChanges();

      expect(fixture.debugElement.query(By.css('#authenticated-admin-navbar-end'))).toBeFalsy();
    }));

  });

});
