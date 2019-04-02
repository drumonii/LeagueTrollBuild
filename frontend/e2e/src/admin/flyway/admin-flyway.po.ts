import { browser, by, element, ElementFinder } from 'protractor';

import { BaseAdminPage } from '../base-admin.po';

export class AdminFlywayPage extends BaseAdminPage {

  async navigateTo(): Promise<void> {
    await browser.get('/admin/flyway');
  }

  getFlywayDatatable(): ElementFinder {
    return element(by.css('#flyway-datatable'));
  }

}
