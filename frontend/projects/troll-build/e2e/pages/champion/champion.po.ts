import { BaseTrollBuildPage } from '../base-troll-build.po';

export class ChampionPage extends BaseTrollBuildPage {

  navigateTo(): void {
    cy.visit(`/champions/${this.getChampionName()}`);
  }

  navigateToBuild(build: string): void {
    cy.visit(build);
  }

  getChampionName(): string {
    return 'Skarner';
  }

  getChampion(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('#champion-name');
  }

  private getMaps(): Cypress.Chainable<JQuery<HTMLSelectElement>> {
    return cy.get('[data-e2e="map-select"]');
  }

  getDefaultSelectedMap(): Cypress.Chainable<JQuery<HTMLElement>> {
    return this.getMaps().get('.select-button');
  }

  getTrollBuild(): {
    items: () => Cypress.Chainable<JQuery<HTMLElement>>,
    summonerSpells: () => Cypress.Chainable<JQuery<HTMLElement>>,
    trinket: () => Cypress.Chainable<JQuery<HTMLElement>>
  } {
    return {
      items: this.getTrollBuildItems,
      summonerSpells: this.getTrollBuildSummonerSpells,
      trinket: this.getTrollBuildTrinket
    };
  }

  private getTrollBuildItems(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="items-row"] > .ltb-list > .ltb-list-item');
  }

  private getTrollBuildSummonerSpells(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="summoner-spells-row"] > .ltb-list > .ltb-list-item');
  }

  private getTrollBuildTrinket(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="trinket-row"] > .ltb-list > .ltb-list-item');
  }

  saveTrollBuild(): void {
    this.getSaveBuildBtn().click();
  }

  getSaveBuildBtn(): Cypress.Chainable<JQuery<HTMLParagraphElement>> {
    return cy.get('[data-e2e="save-build-btn"]');
  }

  getSavedBuildInputLink(): Cypress.Chainable<JQuery<HTMLParagraphElement>> {
    return cy.get('[data-e2e="saved-build-input-link"]');
  }

}
