import { browser, by, element } from 'protractor';

import { BaseAdminPage } from './base-admin.po';

export class AdminAppPage extends BaseAdminPage {

  navigateTo() {
    return browser.get('/admin/login');
  }

  getHeaderText() {
    return element(by.css('#admin-header-title')).getText();
  }

  getFooter() {
    return element(by.css('#admin-footer'));
  }

}
