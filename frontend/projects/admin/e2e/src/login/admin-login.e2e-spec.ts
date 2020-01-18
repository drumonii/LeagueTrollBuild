import { AdminLoginPage } from './admin-login.po';

describe('admin login page', () => {
  const page = new AdminLoginPage();

  let usernameInput;
  let passwordInput;

  beforeEach(async () => {
    await page.navigateTo();

    expect(await page.getTitleContent()).toBe('Admin Login');

    usernameInput = page.getUsernameInput();
    passwordInput = page.getPasswordInput();
  });

  describe('with empty username and password', () => {

    it('should not allow logins', async () => {
      expect(await page.getLoginBtn().isEnabled()).toBeFalsy();
    });

  });

  describe('with invalid credentials', () => {

    it('should not login the admin user', async () => {
      await usernameInput.sendKeys(page.getUsername());
      await passwordInput.sendKeys('invalid password!');

      await page.attemptLoginAdmin();
      expect(await page.getInvalidCredentialsAlert().isPresent()).toBeTruthy();
    });

  });

  describe('with valid credentials', () => {

    it('should login and logout the admin user', async () => {
      await usernameInput.sendKeys(page.getUsername());
      await passwordInput.sendKeys(page.getPassword());

      await page.attemptLoginAdmin();
      expect(await page.getCurrentUrl()).toBe('/');

      await page.logoutAdmin();
      expect(await page.getLoggedOutAlert().isPresent()).toBeTruthy();
    });

  });

});
