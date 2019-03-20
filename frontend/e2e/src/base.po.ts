import { browser } from 'protractor';

export abstract class BasePage {

  abstract async navigateTo();

  async getTitle() {
    return await browser.getTitle();
  }

  async getTitleContent() {
    return this.getTitle().then((title) => title.substring(title.indexOf('|') + 2));
  }

  async getCurrentUrl() {
    const url = await browser.getCurrentUrl();
    return this.parseUrl(url);
  }

  getHrefLink(link) {
    return this.parseUrl(link);
  }

  parseUrl(url) {
    return url.substring(url.lastIndexOf('/'));
  }

}
