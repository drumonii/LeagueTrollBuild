import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { By } from '@angular/platform-browser';

import { AdminAuthService } from '@security/admin-auth.service';

import { AdminLoginModule } from './admin-login.module';
import { AdminLoginPage } from './admin-login.page';
import { TitleService } from '@service/title.service';

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

      xit('should show bad credentials alert', inject([AdminAuthService, Router], (authService: AdminAuthService, router: Router) => {
        spyOn(router, 'navigate');

        fixture.detectChanges();

        expect(fixture.debugElement.query(By.css('#login-bad-credentials-alert'))).toBeTruthy();
        expect(router.navigate).not.toHaveBeenCalled();
      }));

    });

    describe('with unexpected login error', () => {

      xit('should show unexpected login error alert', inject([AdminAuthService, Router], (authService: AdminAuthService, router: Router) => {
        spyOn(router, 'navigate');

        fixture.detectChanges();

        expect(fixture.debugElement.query(By.css('#login-unexpected-error-alert'))).toBeTruthy();
        expect(router.navigate).not.toHaveBeenCalled();
      }));

    });

    describe('with correct credentials', () => {

      xit('should redirect to admin home', inject([AdminAuthService, Router], (authService: AdminAuthService, router: Router) => {
        spyOn(router, 'navigate');

        const loginBtn = fixture.debugElement.query(By.css('#login-btn'));
        loginBtn.nativeElement.click();

        fixture.detectChanges();

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
