import { browser, by, element, ElementFinder, ExpectedConditions } from 'protractor';

export abstract class BaseAdminPage {

  abstract async navigateTo();

  async getTitle(): Promise<string> {
    return await browser.getTitle();
  }

  async getTitleContent(): Promise<string> {
    const title = await this.getTitle();
    return title.substring(title.indexOf('|') + 2);
  }

  async getCurrentUrl(): Promise<string> {
    const adminUrl = await browser.getCurrentUrl();
    return this.parseAdminUrl(adminUrl);
  }

  getRedirectedUrl(): string {
    return '/'; // will route to /champions with troll-build app running
  }

  getUsername(): string {
    return 'admin';
  }

  getPassword(): string {
    return 'password'; // see USERS.sql
  }

  getUsernameInput(): ElementFinder {
    return element(by.css('#username-input'));
  }

  getPasswordInput(): ElementFinder {
    return element(by.css('#password-input'));
  }

  async loginAdmin(): Promise<void> {
    await browser.get('/admin/login');

    const usernameInput = this.getUsernameInput();
    await usernameInput.sendKeys(this.getUsername());
    const passwordInput = this.getPasswordInput();
    await passwordInput.sendKeys(this.getPassword());

    await element(by.css('#login-btn')).click();

    const adminNav = element(by.css('#admin-nav'));
    await browser.wait(ExpectedConditions.presenceOf(adminNav));
  }

  async logoutAdmin(): Promise<void> {
    await element(by.css('#admin-logout-navbar-item')).click();
    await element(by.css('#admin-logout-btn')).click();

    const loggedOutAlert = element(by.css('#logged-out-alert'));
    await browser.wait(ExpectedConditions.presenceOf(loggedOutAlert));
  }

  private parseAdminUrl(adminUrl): string {
    const newAdminUrl = adminUrl.substring(adminUrl.lastIndexOf('/'));
    if (newAdminUrl === '/admin') {
      return '/';
    }
    return newAdminUrl;
  }

}
