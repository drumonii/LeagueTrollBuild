import { BaseTrollBuildPage } from '../base-troll-build.po';

export class ChampionsPage extends BaseTrollBuildPage {

  navigateTo(): void {
    cy.visit('/champions');
  }

  getChampions(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('ltb-champion-img');
  }

  getChampionsLength(): Cypress.Chainable<number> {
    return this.getChampions().its('length');
  }

  getFirstChampion(): Cypress.Chainable<JQuery<HTMLElement>> {
    return this.getChampions().first();
  }

  championName(champion: Cypress.Chainable<JQuery<HTMLElement>>): Cypress.Chainable<JQuery<HTMLElement>> {
    return champion.get('[data-e2e="champion-name"]').first();
  }

  getChampionTagFilters(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="champion-tag-filter-btn"]');
  }

  getChampionNameFilter(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="champions-search-input"]');
  }

}
