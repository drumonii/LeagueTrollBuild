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
    return element(by.css('#map-select'));
  }

  getDefaultSelectedMap(): ElementFinder {
    return this.getMaps().element(by.css('option:checked'));
  }

  getTrollBuild() {
    return {
      items: this.getTrollBuildItems,
      summonerSpells: this.getTrollBuildSummonerSpells,
      trinket: this.getTrollBuildTrinket
    };
  }

  private getTrollBuildItems(): ElementArrayFinder {
    return element.all(by.css('.troll-build-item'));
  }

  private getTrollBuildSummonerSpells(): ElementArrayFinder {
    return element.all(by.css('.troll-build-summoner-spell'));
  }

  private getTrollBuildTrinket(): ElementArrayFinder {
    return element.all(by.css('.troll-build-trinket'));
  }

  async saveTrollBuild(): Promise<void> {
    await element(by.css('#save-build-btn')).click();
    const saveBuildInput = await element(by.css('#saved-build-input-link'));
    await browser.wait(ExpectedConditions.presenceOf(saveBuildInput));
  }

  async getSavedBuild(): Promise<string> {
    return element(by.css('#saved-build-input-link')).getText();
  }

}
