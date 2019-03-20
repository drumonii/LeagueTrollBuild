import { browser, by, element } from 'protractor';

import { BaseAdminPage } from '../base-admin.po';

export class AdminLoginPage extends BaseAdminPage {

  navigateTo() {
    return browser.get('/admin/login');
  }

  getLoginBtn() {
    return element(by.css('#login-btn'));
  }

  attemptLoginAdmin() {
    const loginBtn = this.getLoginBtn();
    return loginBtn.click();
  }

  getInvalidCredentialsAlert() {
    return element(by.css('#login-bad-credentials-alert'));
  }

  getLoggedOutAlert() {
    return element(by.css('#logged-out-alert'));
  }

}
