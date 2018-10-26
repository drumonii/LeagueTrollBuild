import { browser, by, element } from 'protractor';

export class TrollBuildAppPage {

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
