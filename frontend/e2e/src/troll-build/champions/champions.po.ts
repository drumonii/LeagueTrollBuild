import { browser, by, element, ElementFinder } from 'protractor';

import { BaseTrollBuildPage } from '../base-troll-build.po';

export class ChampionsPage extends BaseTrollBuildPage {

  navigateTo() {
    return browser.get('/champions');
  }

  getChampions() {
    return element.all(by.css('.champion'));
  }

  getFirstChampion() {
    return this.getChampions().first();
  }

  championName(champion: ElementFinder) {
    return champion.element(by.css('.champion-name')).getText();
  }

  getChampionTagFilters() {
    return element.all(by.css('.champion-tag-btn'));
  }

  getChampionNameFilter() {
    return element(by.css('#champions-search-input'));
  }

}
