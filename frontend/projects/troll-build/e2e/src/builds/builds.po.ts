import { browser } from 'protractor';

import { BaseTrollBuildPage } from '../base-troll-build.po';

export class BuildsPage extends BaseTrollBuildPage {

  async navigateTo(): Promise<void> {
    await browser.get('/builds/100000000');
  }

}
