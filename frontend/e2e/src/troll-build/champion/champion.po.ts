import { browser, by, element, ExpectedConditions } from 'protractor';

import { BaseTrollBuildPage } from '../base-troll-build.po';

export class ChampionPage extends BaseTrollBuildPage {

  navigateTo() {
    return browser.get(`/champions/${this.getChampionName()}`);
  }

  navigateToBuild(build: string) {
    return browser.get(`${build}`);
  }

  getChampionName() {
    return 'Skarner';
  }

  getChampion() {
    return element(by.css('#champion-name'));
  }

  private getMaps() {
    return element(by.css('#map-select'));
  }

  getDefaultSelectedMap() {
    return this.getMaps().element(by.css('option:checked'));
  }

  getTrollBuild() {
    return {
      items: this.getTrollBuildItems,
      summonerSpells: this.getTrollBuildSummonerSpells,
      trinket: this.getTrollBuildTrinket
    };
  }

  private getTrollBuildItems() {
    return element.all(by.css('.troll-build-item'));
  }

  private getTrollBuildSummonerSpells() {
    return element.all(by.css('.troll-build-summoner-spell'));
  }

  private getTrollBuildTrinket() {
    return element.all(by.css('.troll-build-trinket'));
  }

  saveTrollBuild() {
    element(by.css('#save-build-btn')).click();
    const saveBuildInput = element(by.css('#saved-build-input-link'));
    browser.wait(ExpectedConditions.presenceOf(saveBuildInput));
  }

  getSavedBuild() {
    return element(by.css('#saved-build-input-link')).getText();
  }

}
