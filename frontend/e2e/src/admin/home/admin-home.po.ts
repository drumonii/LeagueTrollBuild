import { browser, by, element } from 'protractor';

import { BaseAdminPage } from '../base-admin.po';

export class AdminHomePage extends BaseAdminPage {

  navigateTo() {
    return browser.get('/admin');
  }

  getFailedJobsComponent() {
    return element(by.css('#failed-jobs-card'));
  }

  getFailedToLoadFailedJobsAlert() {
    return element(by.css('#no-failed-jobs-alert'));
  }

  getCpuUsageComponent() {
    return element(by.css('#cpu-usage-card'));
  }

  getFailedToLoadCpuUsageAlert() {
    return element(by.css('#no-cpu-usage-alert'));
  }

  getMemoryUsageComponent() {
    return element(by.css('#memory-usage-card'));
  }

  getFailedToLoadMemoryUsageAlert() {
    return element(by.css('#no-memory-usage-alert'));
  }

  getGlobalErrorsComponent() {
    return element(by.css('#global-errors-card'));
  }

  getFailedToLoadGlobalErrorsAlert() {
    return element(by.css('#no-global-errors-alert'));
  }

}
