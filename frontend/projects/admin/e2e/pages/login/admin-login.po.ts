import { BaseAdminPage } from '../base-admin.po';

export class AdminLoginPage extends BaseAdminPage {

  navigateTo(): void {
    cy.visit('/admin/login');
  }

  getLoginBtn(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="login-btn"]');
  }

  attemptLoginAdmin(): Cypress.Chainable<JQuery<HTMLElement>> {
    return this.getLoginBtn().click();
  }

  attemptLogoutAdmin(): void {
    cy.get('clr-dropdown button.dropdown-toggle').click();
    cy.get('clr-dropdown-menu button.dropdown-item').click();
  }

  getInvalidCredentialsAlert(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="login-bad-credentials-alert"]');
  }

  getLoggedOutAlert(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="logged-out-alert"]');
  }

}
