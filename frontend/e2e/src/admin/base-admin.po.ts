import { browser, by, element, ExpectedConditions } from 'protractor';

import { BasePage } from '../base.po';

export abstract class BaseAdminPage extends BasePage {

  getCurrentUrl() {
    return super.getCurrentUrl().then((adminUrl) => this.parseAdminUrl(adminUrl));
  }

  getHrefLink(link) {
    return super.getHrefLink(link).then((adminUrl) => this.parseAdminUrl(adminUrl));
  }

  getRedirectedUrl() {
    return '/champions';
  }

  getUsername() {
    return 'admin';
  }

  getPassword() {
    return 'password'; // see USERS.sql
  }

  getUsernameInput() {
    return element(by.css('#username-input'));
  }

  getPasswordInput() {
    return element(by.css('#password-input'));
  }

  loginAdmin() {
    browser.get('/admin/login');

    const usernameInput = this.getUsernameInput();
    usernameInput.sendKeys(this.getUsername());
    const passwordInput = this.getPasswordInput();
    passwordInput.sendKeys(this.getPassword());

    const loginBtn = element(by.css('#login-btn'));
    loginBtn.click();

    const adminNav = element(by.css('#admin-nav'));
    browser.wait(ExpectedConditions.presenceOf(adminNav));
  }

  logoutAdmin() {
    const logoutNavbarItem = element(by.css('#admin-logout-navbar-item'));
    logoutNavbarItem.click();

    const logoutBtn = element(by.css('#admin-logout-btn'));
    logoutBtn.click();

    const loggedOutAlert = element(by.css('#logged-out-alert'));
    browser.wait(ExpectedConditions.presenceOf(loggedOutAlert));
  }

  private parseAdminUrl(adminUrl) {
    const newAdminUrl = adminUrl.substring(adminUrl.lastIndexOf('/'));
    if (newAdminUrl === '/admin') {
      return '/';
    }
    return newAdminUrl;
  }

}
