import { browser, by, element } from 'protractor';

import { BaseAdminPage } from '../base-admin.po';

export class AdminBatchPage extends BaseAdminPage {

  navigateTo() {
    return browser.get('/admin/batch');
  }

  getBatchJobsDatatable() {
    return element(by.css('#batch-jobs-datatable'));
  }

}
