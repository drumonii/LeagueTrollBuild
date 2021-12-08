import { BaseAdminPage } from './base-admin.po';

export class AdminAppPage extends BaseAdminPage {

  navigateTo(): void {
    cy.visit('/admin/login');
  }

  getHeaderText(): Cypress.Chainable<string> {
    return cy.get('#admin-header-title')
      .then((title) => title.text());
  }

  getFooter(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('#admin-footer');
  }

}
