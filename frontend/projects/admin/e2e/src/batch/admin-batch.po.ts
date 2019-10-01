import { browser, by, element, ElementFinder } from 'protractor';

import { BaseAdminPage } from '../base-admin.po';

export class AdminBatchPage extends BaseAdminPage {

  async navigateTo(): Promise<void> {
    await browser.get('/admin/batch');
  }

  getBatchJobsDatatable(): ElementFinder {
    return element(by.css('[data-e2e="batch-jobs-datatable"]'));
  }

  getStepExecutionsDetail(): ElementFinder {
    return element(by.css('[data-e2e="step-executions-table"]'));
  }

}
