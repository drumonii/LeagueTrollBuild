import { browser, by, element, ElementArrayFinder, ElementFinder, ExpectedConditions } from 'protractor';

import { BaseTrollBuildPage } from '../base-troll-build.po';

export class ChampionPage extends BaseTrollBuildPage {

  async navigateTo(): Promise<void> {
    await browser.get(`/champions/${this.getChampionName()}`);
  }

  async navigateToBuild(build: string): Promise<void> {
    await browser.get(`${build}`);
  }

  getChampionName(): string {
    return 'Skarner';
  }

  getChampion(): ElementFinder {
    return element(by.css('#champion-name'));
  }

  private getMaps(): ElementFinder {
    return element(by.css('[data-e2e="map-select"]'));
  }

  getDefaultSelectedMap(): ElementFinder {
    return this.getMaps().element(by.css('.select-button'));
  }

  getTrollBuild() {
    return {
      items: this.getTrollBuildItems,
      summonerSpells: this.getTrollBuildSummonerSpells,
      trinket: this.getTrollBuildTrinket
    };
  }

  private getTrollBuildItems(): ElementArrayFinder {
    return element.all(by.css('[data-e2e="items-row"] > .ltb-list > .ltb-list-item'));
  }

  private getTrollBuildSummonerSpells(): ElementArrayFinder {
    return element.all(by.css('[data-e2e="summoner-spells-row"] > .ltb-list > .ltb-list-item'));
  }

  private getTrollBuildTrinket(): ElementArrayFinder {
    return element.all(by.css('[data-e2e="trinket-row"] > .ltb-list > .ltb-list-item'));
  }

  async saveTrollBuild(): Promise<void> {
    await this.getSaveBuildBtn().click();
    await browser.wait(ExpectedConditions.presenceOf(this.getSavedBuildInputLink()));
  }

  getSaveBuildBtn(): ElementFinder {
    return element(by.css('[data-e2e="save-build-btn"]'));
  }

  getSavedBuildInputLink(): ElementFinder {
    return element(by.css('[data-e2e="saved-build-input-link"]'));
  }

}
