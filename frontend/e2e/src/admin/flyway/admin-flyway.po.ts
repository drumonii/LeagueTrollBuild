import { browser, by, element } from 'protractor';

import { BaseAdminPage } from '../base-admin.po';

export class AdminFlywayPage extends BaseAdminPage {

  navigateTo() {
    return browser.get('/admin/flyway');
  }

  getFlywayDatatable() {
    return element(by.css('#flyway-datatable'));
  }

}
