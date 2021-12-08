import { BaseAdminPage } from '../base-admin.po';

export class AdminHomePage extends BaseAdminPage {

  navigateTo(): void {
    cy.visit('/admin');
  }

  getAppHealthComponent(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="app-health-card"]');
  }

  getAppHealthErrorAlert(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="error-app-health-alert"]');
  }

  getEnvComponent(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="env-card"]');
  }

  getEnvErrorAlert(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="error-env-alert"]');
  }

  getResourcesComponent(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="resources-card"]');
  }

  getResourcesErrorAlert(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="error-resources-alert"]');
  }

  getHttpStatsComponent(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="http-stats-card"]');
  }

  getHttpStatsErrorAlert(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="error-http-stats-alert"]');
  }

}
