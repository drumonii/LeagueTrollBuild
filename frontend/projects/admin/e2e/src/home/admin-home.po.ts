import { browser, by, element, ElementFinder } from 'protractor';

import { BaseAdminPage } from '../base-admin.po';

export class AdminHomePage extends BaseAdminPage {

  async navigateTo(): Promise<void> {
    await browser.get('/admin');
  }

  getFailedJobsComponent(): ElementFinder {
    return element(by.css('[data-e2e="failed-jobs-card"]'));
  }

  getFailedToLoadFailedJobsAlert(): ElementFinder {
    return element(by.css('[data-e2e="no-failed-jobs-alert"]'));
  }

  getCpuUsageComponent(): ElementFinder {
    return element(by.css('[data-e2e="cpu-usage-card"]'));
  }

  getFailedToLoadCpuUsageAlert(): ElementFinder {
    return element(by.css('[data-e2e="no-cpu-usage-alert"]'));
  }

  getMemoryUsageComponent(): ElementFinder {
    return element(by.css('[data-e2e="memory-usage-card"]'));
  }

  getFailedToLoadMemoryUsageAlert(): ElementFinder {
    return element(by.css('[data-e2e="no-memory-usage-alert"]'));
  }

  getGlobalErrorsComponent(): ElementFinder {
    return element(by.css('[data-e2e="global-errors-card"]'));
  }

  getFailedToLoadGlobalErrorsAlert(): ElementFinder {
    return element(by.css('[data-e2e="no-global-errors-alert"]'));
  }

}
