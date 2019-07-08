import { browser, by, element, ElementFinder } from 'protractor';

import { BaseAdminPage } from '../base-admin.po';

export class AdminBatchPage extends BaseAdminPage {

  async navigateTo(): Promise<void> {
    await browser.get('/admin/batch');
  }

  getBatchJobsDatatable(): ElementFinder {
    return element(by.css('#batch-jobs-datatable'));
  }

  getStepExecutionsDetail(): ElementFinder {
    return element(by.css('.step-executions-table'));
  }

}
