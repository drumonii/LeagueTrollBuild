import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { AdminAuthService } from '@security/admin-auth.service';
import { TitleService } from '@service/title.service';
import { AdminLoginModule } from './admin-login.module';
import { AdminLoginPage } from './admin-login.page';
import { AdminLoginResponse, AdminLoginStatus } from '@security/admin-login-response';

describe('AdminLoginPage', () => {
  let component: AdminLoginPage;
  let fixture: ComponentFixture<AdminLoginPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdminLoginModule, HttpClientTestingModule, RouterTestingModule]
    })
    .compileComponents();
  }));

  beforeEach(inject([TitleService], (title: TitleService) => {
    fixture = TestBed.createComponent(AdminLoginPage);
    component = fixture.componentInstance;

    spyOn(title, 'setTitle').and.callThrough();
  }));

  afterEach(inject([TitleService], (title: TitleService) => {
    expect(title.setTitle).toHaveBeenCalledWith('Admin Login');
  }));

  it('should show login form', () => {
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('#admin-login-form'))).toBeTruthy();
    expect(component.adminLoginForm.valid).toBe(false);

    const usernameInput = fixture.debugElement.query(By.css('#username-input'));
    expect(usernameInput.nativeElement.placeholder).toBe('Username');
    expect(usernameInput.nativeElement.type).toBe('text');

    const passwordInput = fixture.debugElement.query(By.css('#password-input'));
    expect(passwordInput.nativeElement.placeholder).toBe('Password');
    expect(passwordInput.nativeElement.type).toBe('password');

    const loginBtn = fixture.debugElement.query(By.css('#login-btn'));
    expect(loginBtn.nativeElement.textContent.trim()).toBe('Login');
    expect(loginBtn.nativeElement.disabled).toBe(true);
  });

  describe('with form errors', () => {

    it('from invalid username', () => {
      const username = component.adminLoginForm.get('username');
      username.setValue(''); username.markAsTouched();

      fixture.detectChanges();

      expect(fixture.debugElement.query(By.css('#invalid-username-feedback'))).toBeTruthy();

      const usernameInput = fixture.debugElement.query(By.css('#username-input'));
      expect(usernameInput.classes['is-danger']).toBeTruthy();
    });

    it('from invalid password', () => {
      const password = component.adminLoginForm.get('password');
      password.setValue(''); password.markAsTouched();

      fixture.detectChanges();

      expect(fixture.debugElement.query(By.css('#invalid-password-feedback'))).toBeTruthy();

      const passwordInput = fixture.debugElement.query(By.css('#password-input'));
      expect(passwordInput.classes['is-danger']).toBeTruthy();
    });

  });

  describe('with valid form', () => {

    describe('with bad credentials', () => {

      it('should show bad credentials alert', inject([AdminAuthService, Router], (authService: AdminAuthService, router: Router) => {
        const failedLoginResponse: AdminLoginResponse = {
          status: AdminLoginStatus.FAILED,
          message: 'Bad credentials',
        };

        spyOn(authService, 'loginAdmin').and.returnValue(of(failedLoginResponse));
        spyOn(router, 'navigate');

        component.unexpectedError = true;
        component.loggedOut = true;

        const username = component.adminLoginForm.get('username');
        username.setValue('some_username'); username.markAsTouched();

        const password = component.adminLoginForm.get('password');
        password.setValue('some_password'); password.markAsTouched();

        fixture.detectChanges();

        const loginBtn = fixture.debugElement.query(By.css('#login-btn'));
        loginBtn.nativeElement.click();

        fixture.detectChanges();

        expect(fixture.debugElement.query(By.css('#login-bad-credentials-alert'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('#login-unexpected-error-alert'))).toBeFalsy();
        expect(fixture.debugElement.query(By.css('#logged-out-alert'))).toBeFalsy();

        expect(authService.loginAdmin).toHaveBeenCalledWith(username.value, password.value);
        expect(router.navigate).not.toHaveBeenCalled();
      }));

    });

    describe('with unexpected login error', () => {

      it('should show unexpected login error alert', inject([AdminAuthService, Router], (authService: AdminAuthService, router: Router) => {
        spyOn(authService, 'loginAdmin').and.returnValue(of(null));
        spyOn(router, 'navigate');

        component.badCredentials = true;
        component.loggedOut = true;

        const username = component.adminLoginForm.get('username');
        username.setValue('some_username'); username.markAsTouched();

        const password = component.adminLoginForm.get('password');
        password.setValue('some_password'); password.markAsTouched();

        fixture.detectChanges();

        const loginBtn = fixture.debugElement.query(By.css('#login-btn'));
        loginBtn.nativeElement.click();

        fixture.detectChanges();

        expect(fixture.debugElement.query(By.css('#login-bad-credentials-alert'))).toBeFalsy();
        expect(fixture.debugElement.query(By.css('#login-unexpected-error-alert'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('#logged-out-alert'))).toBeFalsy();

        expect(authService.loginAdmin).toHaveBeenCalledWith(username.value, password.value);
        expect(router.navigate).not.toHaveBeenCalled();
      }));

    });

    describe('with correct credentials', () => {

      it('should redirect to admin home', inject([AdminAuthService, Router], (authService: AdminAuthService, router: Router) => {
        const successfulLoginResponse: AdminLoginResponse = {
          status: AdminLoginStatus.SUCCESS,
          message: 'Login successful',
          userDetails: {
            username: 'admin',
            authorities: [
              {
                authority: 'ROLE_ADMIN'
              }
            ]
          }
        };

        spyOn(authService, 'loginAdmin').and.returnValue(of(successfulLoginResponse));
        spyOn(router, 'navigate');

        const username = component.adminLoginForm.get('username');
        username.setValue('some_username'); username.markAsTouched();

        const password = component.adminLoginForm.get('password');
        password.setValue('some_password'); password.markAsTouched();

        fixture.detectChanges();

        const loginBtn = fixture.debugElement.query(By.css('#login-btn'));
        loginBtn.nativeElement.click();

        fixture.detectChanges();

        expect(authService.loginAdmin).toHaveBeenCalledWith(username.value, password.value);
        expect(router.navigate).toHaveBeenCalledWith(['/admin']);
      }));

    });

  });

  describe('with logged out', () => {

    it('should show logged out alert', () => {
      component.loggedOut = true;

      fixture.detectChanges();

      expect(fixture.debugElement.query(By.css('#logged-out-alert'))).toBeTruthy();
    });

  });

});
