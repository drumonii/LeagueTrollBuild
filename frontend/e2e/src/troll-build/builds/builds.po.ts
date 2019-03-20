import { browser } from 'protractor';

import { BaseTrollBuildPage } from '../base-troll-build.po';

export class BuildsPage extends BaseTrollBuildPage {

  navigateTo() {
    return browser.get('/builds/100000000');
  }

}
