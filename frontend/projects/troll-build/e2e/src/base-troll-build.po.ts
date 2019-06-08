import { browser } from 'protractor';

export abstract class BaseTrollBuildPage {

  abstract async navigateTo();

  async getTitle(): Promise<string> {
    return await browser.getTitle();
  }

  async getTitleContent(): Promise<string> {
    const title = await this.getTitle();
    return title.substring(title.indexOf('|') + 2);
  }

  async getCurrentUrl(): Promise<string> {
    const url = await browser.getCurrentUrl();
    return this.parseUrl(url);
  }

  getHrefLink(link): string {
    return this.parseUrl(link);
  }

  parseUrl(url): string {
    return url.substring(url.lastIndexOf('/'));
  }

}
