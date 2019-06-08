import { browser, by, element, ElementFinder } from 'protractor';

import { BaseAdminPage } from './base-admin.po';

export class AdminAppPage extends BaseAdminPage {

  async navigateTo(): Promise<void> {
    await browser.get('/admin/login');
  }

  async getHeaderText(): Promise<string> {
    return element(by.css('#admin-header-title')).getText();
  }

  getFooter(): ElementFinder {
    return element(by.css('#admin-footer'));
  }

}
