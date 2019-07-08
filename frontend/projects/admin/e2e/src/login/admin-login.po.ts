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
    return element(by.css('clr-alert .alert.alert-danger'));
  }

  getLoggedOutAlert(): ElementFinder {
    return element(by.css('clr-alert .alert.alert-success'));
  }

}
