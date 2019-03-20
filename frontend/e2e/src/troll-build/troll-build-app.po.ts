import { browser, by, element } from 'protractor';

import { BaseTrollBuildPage } from './base-troll-build.po';

export class TrollBuildAppPage extends BaseTrollBuildPage {

  navigateTo() {
    return browser.get('/');
  }

  getHeaderText() {
    return element(by.css('#header-title')).getText();
  }

  getFooter() {
    return element(by.css('#footer'));
  }

}
