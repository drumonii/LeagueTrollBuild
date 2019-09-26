import { browser, by, element, ElementArrayFinder, ElementFinder } from 'protractor';

import { BaseTrollBuildPage } from '../base-troll-build.po';

export class ChampionsPage extends BaseTrollBuildPage {

  async navigateTo(): Promise<void> {
    await browser.get('/champions');
  }

  getChampions(): ElementArrayFinder {
    return element.all(by.css('ltb-champion-img'));
  }

  getFirstChampion(): ElementFinder {
    return this.getChampions().first();
  }

  async championName(champion: ElementFinder): Promise<string> {
    return champion.element(by.css('[data-e2e="champion-name"]')).getText();
  }

  getChampionTagFilters(): ElementArrayFinder {
    return element.all(by.css('[data-e2e="champion-tag-filter-btn"]'));
  }

  getChampionNameFilter(): ElementFinder {
    return element(by.css('[data-e2e="champions-search-input"]'));
  }

}
