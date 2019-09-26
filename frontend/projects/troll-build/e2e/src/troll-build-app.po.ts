import { browser, by, element, ElementFinder } from 'protractor';

import { BaseTrollBuildPage } from './base-troll-build.po';

export class TrollBuildAppPage extends BaseTrollBuildPage {

  async navigateTo(): Promise<void> {
    await browser.get('/');
  }

  async getHeaderText(): Promise<string> {
    return element(by.css('.header-title')).getText();
  }

  getFooter(): ElementFinder {
    return element(by.css('footer'));
  }

}
