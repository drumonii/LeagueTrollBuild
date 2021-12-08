import { ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, convertToParamMap, Router } from '@angular/router';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { AdminLoginModule } from './admin-login.module';
import { AdminLoginPage } from './admin-login.page';
import { AdminAuthService } from '../security/admin-auth.service';
import { AdminTitleService } from '@admin-service/admin-title.service';
import { AdminLoginResponse, AdminLoginStatus } from '../security/admin-login-response';

describe('AdminLoginPage', () => {
  let component: AdminLoginPage;
  let fixture: ComponentFixture<AdminLoginPage>;

  describe('admin login form', () => {

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        imports: [HttpClientTestingModule, NoopAnimationsModule, RouterTestingModule, AdminLoginModule]
      })
      .compileComponents();
    });

    beforeEach(inject([AdminTitleService, Router], (title: AdminTitleService, router: Router) => {
      fixture = TestBed.createComponent(AdminLoginPage);
      component = fixture.componentInstance;

      spyOn(title, 'setTitle').and.callThrough();
      spyOn(router, 'navigate');

      fixture.detectChanges();
    }));

    afterEach(inject([AdminTitleService], (title: AdminTitleService) => {
      expect(title.setTitle).toHaveBeenCalledWith('Admin Login');
    }));

    it('should show login form', () => {
      expect(fixture.debugElement.query(By.css('[data-e2e="admin-login-form"]'))).toBeTruthy();
      expect(component.adminLoginForm.valid).toBeFalse();

      const usernameInput = getUsernameInput();
      expect(usernameInput.nativeElement.placeholder).toBe('Username');
      expect(usernameInput.nativeElement.type).toBe('text');

      const passwordInput = getPasswordInput();
      expect(passwordInput.nativeElement.placeholder).toBe('Password');
      expect(passwordInput.nativeElement.type).toBe('password');

      const loginBtn = getLoginBtn();
      expect(loginBtn.nativeElement.textContent.trim()).toBe('Login');
      expect(loginBtn.nativeElement.disabled).toBeTrue();
    });

    describe('with form errors', () => {

      it('from invalid username', () => {
        const username = component.adminLoginForm.get('username');
        username.setValue(''); username.markAsTouched();
        const usernameInput = getUsernameInput();
        usernameInput.triggerEventHandler('blur', null);

        fixture.detectChanges();

        expect(fixture.debugElement.query(By.css('[data-e2e="username-input-container"] clr-control-error'))).toBeTruthy();
      });

      it('from invalid password', () => {
        const password = component.adminLoginForm.get('password');
        password.setValue(''); password.markAsTouched();
        const passwordInput = getPasswordInput();
        passwordInput.triggerEventHandler('blur', null);

        fixture.detectChanges();

        expect(fixture.debugElement.query(By.css('[data-e2e="password-input-container"] clr-control-error'))).toBeTruthy();
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

          const username = component.adminLoginForm.get('username');
          username.setValue('some_username'); username.markAsTouched();

          const password = component.adminLoginForm.get('password');
          password.setValue('some_password'); password.markAsTouched();

          fixture.detectChanges();

          const loginBtn = getLoginBtn();
          loginBtn.nativeElement.click();

          fixture.detectChanges();

          expect(getBadCredentialsAlert()).toBeTruthy();
          expect(getUnexpectedErrorAlert()).toBeFalsy();
          expect(getLoggedOutAlert()).toBeFalsy();

          expect(authService.loginAdmin).toHaveBeenCalledWith(username.value, password.value);
          expect(router.navigate).toHaveBeenCalledWith(['/admin/login']);
          expect(router.navigate).not.toHaveBeenCalledWith(['/admin']);
        }));

      });

      describe('with unexpected login error', () => {

        it('should show unexpected login error alert',
          inject([AdminAuthService, Router], (authService: AdminAuthService, router: Router) => {
          spyOn(authService, 'loginAdmin').and.returnValue(of(null));

          const username = component.adminLoginForm.get('username');
          username.setValue('some_username'); username.markAsTouched();

          const password = component.adminLoginForm.get('password');
          password.setValue('some_password'); password.markAsTouched();

          fixture.detectChanges();

          const loginBtn = getLoginBtn();
          loginBtn.nativeElement.click();

          fixture.detectChanges();

          expect(getBadCredentialsAlert()).toBeFalsy();
          expect(getUnexpectedErrorAlert()).toBeTruthy();
          expect(getLoggedOutAlert()).toBeFalsy();

          expect(authService.loginAdmin).toHaveBeenCalledWith(username.value, password.value);
          expect(router.navigate).toHaveBeenCalledWith(['/admin/login']);
          expect(router.navigate).not.toHaveBeenCalledWith(['/admin']);
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

          const username = component.adminLoginForm.get('username');
          username.setValue('some_username'); username.markAsTouched();

          const password = component.adminLoginForm.get('password');
          password.setValue('some_password'); password.markAsTouched();

          fixture.detectChanges();

          const loginBtn = getLoginBtn();
          loginBtn.nativeElement.click();

          fixture.detectChanges();

          expect(authService.loginAdmin).toHaveBeenCalledWith(username.value, password.value);
          expect(router.navigate).toHaveBeenCalledWith(['/admin/login']);
          expect(router.navigate).toHaveBeenCalledWith(['/admin']);
        }));

      });

    });

    function getUsernameInput() {
      return fixture.debugElement.query(By.css('[data-e2e="username-input"]'));
    }

    function getPasswordInput() {
      return fixture.debugElement.query(By.css('[data-e2e="password-input"]'));
    }

    function getLoginBtn() {
      return fixture.debugElement.query(By.css('[data-e2e="login-btn"]'));
    }

    function getBadCredentialsAlert() {
      return fixture.debugElement.query(By.css('[data-e2e="login-bad-credentials-alert"]'));
    }

    function getUnexpectedErrorAlert() {
      return fixture.debugElement.query(By.css('[data-e2e="login-unexpected-error-alert"]'));
    }

    function getLoggedOutAlert() {
      return fixture.debugElement.query(By.css('[data-e2e="logged-out-alert"]'));
    }

  });

  describe('with logged out', () => {

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        imports: [HttpClientTestingModule, NoopAnimationsModule, RouterTestingModule, AdminLoginModule],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              queryParamMap: of(convertToParamMap({ logout: true }))
            }
          }
        ]
      })
      .compileComponents();
    });

    beforeEach(() => {
      fixture = TestBed.createComponent(AdminLoginPage);
      component = fixture.componentInstance;
    });

    it('should show logged out alert', () => {
      fixture.detectChanges();

      expect(fixture.debugElement.query(By.css('[data-e2e="logged-out-alert"]'))).toBeTruthy();
    });

  });

});
