import { browser, by, element } from 'protractor';

import { BaseTrollBuildPage } from '../base-troll-build.po';

export class NotFoundPage extends BaseTrollBuildPage {

  navigateTo() {
    return browser.get('/not-found');
  }

  getMsgHeader() {
    return element(by.css('#title'));
  }

  getMsgBody() {
    return element(by.css('#msg'));
  }

  getReturnToHomeLink() {
    return element(by.css('#return-to-home'));
  }

}
