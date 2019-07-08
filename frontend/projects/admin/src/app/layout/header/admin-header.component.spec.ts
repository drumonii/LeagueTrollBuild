import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { Router, RouterLinkWithHref } from '@angular/router';
import { By } from '@angular/platform-browser';

import { MainContainerWillyWonka } from '@clr/angular';

import { of } from 'rxjs';

import { AdminHeaderModule } from './admin-header.module';
import { AdminHeaderComponent } from './admin-header.component';
import { AdminAuthService } from '@admin-security/admin-auth.service';
import { AdminUserDetails } from '@admin-security/admin-user-details';
import { AdminLogoutResponse, AdminLogoutStatus } from '@admin-security/admin-logout-response';

describe('AdminHeaderComponent', () => {
  let component: AdminHeaderComponent;
  let fixture: ComponentFixture<AdminHeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule, AdminHeaderModule],
      providers: [MainContainerWillyWonka] // to prevent - Error: clr-header should only be used inside of a clr-main-container
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

    const adminHeaderTitle = fixture.debugElement.query(By.css('#admin-header-title'));
    expect(adminHeaderTitle.nativeElement.textContent.trim()).toBe(component.header);
    const adminHeaderTitleLink = adminHeaderTitle.injector.get(RouterLinkWithHref);
    expect(adminHeaderTitleLink.href).toBe('/admin/login');
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

    it('should show navbar-item items', () => {
      const batchNavbarItem = fixture.debugElement.query(By.css('#batch-navbar-item'));
      expect(batchNavbarItem.nativeElement.textContent.trim()).toBe('Batch');
      const batchNavbarItemLink = batchNavbarItem.injector.get(RouterLinkWithHref);
      expect(batchNavbarItemLink.href).toBe('/admin/batch');

      const flywayNavbarItem = fixture.debugElement.query(By.css('#flyway-navbar-item'));
      expect(flywayNavbarItem.nativeElement.textContent.trim()).toBe('Flyway');
      const flywayNavbarItemLink = flywayNavbarItem.injector.get(RouterLinkWithHref);
      expect(flywayNavbarItemLink.href).toBe('/admin/flyway');
    });

    it('should show navbar end dropdown with logout', () => {
      const userDropdown = fixture.debugElement.query(By.css('clr-dropdown'));
      expect(userDropdown).toBeTruthy();

      const userDropdownToggle = userDropdown.query(By.css('button.dropdown-toggle'));
      userDropdownToggle.triggerEventHandler('click', null);

      fixture.detectChanges();

      const username = userDropdown.query(By.css('span.dropdown-item'));
      expect(username.nativeElement.textContent.trim()).toBe(adminUserDetails.username.toUpperCase());

      expect(userDropdown.query(By.css('button.dropdown-item'))).toBeTruthy();
    });

    it('should logout user on logout button click',
      inject([AdminAuthService, Router], (authService: AdminAuthService, router: Router) => {
      const successfulLogoutResponse: AdminLogoutResponse = {
        status: AdminLogoutStatus.SUCCESS,
        message: 'Logout successful',
        userDetails: adminUserDetails
      };
      spyOn(authService, 'logoutAdmin').and.returnValue(of(successfulLogoutResponse));
      spyOn(router, 'navigate');

      const userDropdown = fixture.debugElement.query(By.css('clr-dropdown'));

      const userDropdownToggle = userDropdown.query(By.css('button.dropdown-toggle'));
      userDropdownToggle.triggerEventHandler('click', null);

      fixture.detectChanges();

      const adminLogoutBtn = userDropdown.query(By.css('button.dropdown-item'));
      adminLogoutBtn.triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(authService.logoutAdmin).toHaveBeenCalled();
      expect(router.navigate).toHaveBeenCalledWith(['/admin/login'], { queryParams: { logout: true } });
    }));

  });

  describe('with unauthenticated admin', () => {

    it('should hide navbar end dropdown with logout', inject([AdminAuthService], (authService: AdminAuthService) => {
      spyOnProperty(authService, 'adminUserDetails').and.returnValue(of(null));

      fixture.detectChanges();

      expect(fixture.debugElement.query(By.css('#burger-collapse'))).toBeFalsy();
      expect(fixture.debugElement.query(By.css('#authenticated-admin-navbar-end'))).toBeFalsy();
    }));

  });

});
