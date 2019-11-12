import { browser, by, element, ElementFinder } from 'protractor';

import { BaseAdminPage } from '../base-admin.po';

export class AdminHomePage extends BaseAdminPage {

  async navigateTo(): Promise<void> {
    await browser.get('/admin');
  }

  getAppHealthComponent(): ElementFinder {
    return element(by.css('[data-e2e="app-health-card"]'));
  }

  getAppHealthErrorAlert(): ElementFinder {
    return element(by.css('[data-e2e="error-app-health-alert"]'));
  }

  getEnvComponent(): ElementFinder {
    return element(by.css('[data-e2e="env-card"]'));
  }

  getEnvErrorAlert(): ElementFinder {
    return element(by.css('[data-e2e="error-env-alert"]'));
  }

  getResourcesComponent(): ElementFinder {
    return element(by.css('[data-e2e="resources-card"]'));
  }

  getResourcesErrorAlert(): ElementFinder {
    return element(by.css('[data-e2e="error-resources-alert"]'));
  }

  getHttpStatsComponent(): ElementFinder {
    return element(by.css('[data-e2e="http-stats-card"]'));
  }

  getHttpStatsErrorAlert(): ElementFinder {
    return element(by.css('[data-e2e="error-http-stats-alert"]'));
  }

}
