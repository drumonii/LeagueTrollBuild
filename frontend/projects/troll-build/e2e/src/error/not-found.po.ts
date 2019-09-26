import { browser, by, element, ElementFinder } from 'protractor';

import { BaseTrollBuildPage } from '../base-troll-build.po';

export class NotFoundPage extends BaseTrollBuildPage {

  async navigateTo(): Promise<void> {
    await browser.get('/not-found');
  }

  getMsgHeader(): ElementFinder {
    return element(by.css('[data-e2e="title"]'));
  }

  getMsgBody(): ElementFinder {
    return element(by.css('[data-e2e="msg"]'));
  }

  getReturnToHomeLink(): ElementFinder {
    return element(by.css('[data-e2e="return-to-home"]'));
  }

}
