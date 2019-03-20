import { AdminLoginPage } from './admin-login.po';

describe('admin login page', () => {
  const page = new AdminLoginPage();

  let usernameInput;
  let passwordInput;

  beforeEach(() => {
    page.navigateTo();

    expect(page.getTitleContent()).toBe('Admin Login');

    usernameInput = page.getUsernameInput();
    passwordInput = page.getPasswordInput();
  });

  describe('with empty username and password', () => {

    it('should not allow logins', () => {
      expect(page.getLoginBtn().isEnabled()).toBe(false);
    });

  });

  describe('with invalid credentials', () => {

    it('should not login the admin user', () => {
      usernameInput.sendKeys(page.getUsername());
      passwordInput.sendKeys('invalid password!');

      page.attemptLoginAdmin();
      expect(page.getInvalidCredentialsAlert().isPresent()).toBe(true);
    });

  });

  describe('with valid credentials', () => {

    it('should login and logout the admin user', () => {
      usernameInput.sendKeys(page.getUsername());
      passwordInput.sendKeys(page.getPassword());

      page.attemptLoginAdmin();
      expect(page.getCurrentUrl()).toBe('/');

      page.logoutAdmin();
      expect(page.getLoggedOutAlert().isPresent()).toBe(true);
    });

  });

});
