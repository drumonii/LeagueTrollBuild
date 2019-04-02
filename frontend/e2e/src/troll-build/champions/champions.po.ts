import { browser, by, element, ElementArrayFinder, ElementFinder } from 'protractor';

import { BaseTrollBuildPage } from '../base-troll-build.po';

export class ChampionsPage extends BaseTrollBuildPage {

  async navigateTo(): Promise<void> {
    await browser.get('/champions');
  }

  getChampions(): ElementArrayFinder {
    return element.all(by.css('.champion'));
  }

  getFirstChampion(): ElementFinder {
    return this.getChampions().first();
  }

  async championName(champion: ElementFinder): Promise<string> {
    return champion.element(by.css('.champion-name')).getText();
  }

  getChampionTagFilters(): ElementArrayFinder {
    return element.all(by.css('.champion-tag-btn'));
  }

  getChampionNameFilter(): ElementFinder {
    return element(by.css('#champions-search-input'));
  }

}
