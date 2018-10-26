import { browser, by, element } from 'protractor';

export class AdminAppPage {

  navigateTo() {
    return browser.get('/admin/login');
  }

  getHeaderText() {
    return element(by.css('#admin-header-title')).getText();
  }

  getFooter() {
    return element(by.css('#admin-footer'));
  }

}
