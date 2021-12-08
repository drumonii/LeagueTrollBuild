import { BaseTrollBuildPage } from '../base-troll-build.po';

export class NotFoundPage extends BaseTrollBuildPage {

  navigateTo(): void {
    cy.visit('/not-found');
  }

  getMsgHeader(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="title"]');
  }

  getMsgBody(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="msg"]');
  }

  getReturnToHomeLink(): Cypress.Chainable<JQuery<HTMLAnchorElement>> {
    return cy.get('[data-e2e="return-to-home"]');
  }

}
