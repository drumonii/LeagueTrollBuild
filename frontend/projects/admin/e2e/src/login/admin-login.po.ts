import { browser, by, element, ElementFinder } from 'protractor';

import { BaseAdminPage } from '../base-admin.po';

export class AdminLoginPage extends BaseAdminPage {

  async navigateTo(): Promise<void> {
    await browser.get('/admin/login');
  }

  getLoginBtn(): ElementFinder {
    return element(by.css('#login-btn'));
  }

  async attemptLoginAdmin(): Promise<void> {
    await this.getLoginBtn().click();
  }

  getInvalidCredentialsAlert(): ElementFinder {
    return element(by.css('#login-bad-credentials-alert'));
  }

  getLoggedOutAlert(): ElementFinder {
    return element(by.css('#logged-out-alert'));
  }

}
