import { BaseTrollBuildPage } from './base-troll-build.po';

export class TrollBuildAppPage extends BaseTrollBuildPage {

  navigateTo(): void {
    cy.visit('/');
  }

  getHeader(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="header-title"]');
  }

  getFooter(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('footer');
  }

  getLatestSavedVersion(): Cypress.Chainable<JQuery<HTMLParagraphElement>> {
    return cy.get('[data-e2e="latest-saved-version"]');
  }

}
