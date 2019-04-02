import { browser, by, element, ElementFinder } from 'protractor';

import { BaseAdminPage } from '../base-admin.po';

export class AdminHomePage extends BaseAdminPage {

  async navigateTo(): Promise<void> {
    await browser.get('/admin');
  }

  getFailedJobsComponent(): ElementFinder {
    return element(by.css('#failed-jobs-card'));
  }

  getFailedToLoadFailedJobsAlert(): ElementFinder {
    return element(by.css('#no-failed-jobs-alert'));
  }

  getCpuUsageComponent(): ElementFinder {
    return element(by.css('#cpu-usage-card'));
  }

  getFailedToLoadCpuUsageAlert(): ElementFinder {
    return element(by.css('#no-cpu-usage-alert'));
  }

  getMemoryUsageComponent(): ElementFinder {
    return element(by.css('#memory-usage-card'));
  }

  getFailedToLoadMemoryUsageAlert(): ElementFinder {
    return element(by.css('#no-memory-usage-alert'));
  }

  getGlobalErrorsComponent(): ElementFinder {
    return element(by.css('#global-errors-card'));
  }

  getFailedToLoadGlobalErrorsAlert(): ElementFinder {
    return element(by.css('#no-global-errors-alert'));
  }

}
