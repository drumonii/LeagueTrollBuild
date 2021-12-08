import { BaseAdminPage } from '../base-admin.po';

export class AdminFlywayPage extends BaseAdminPage {

  navigateTo(): void {
    cy.visit('/admin/flyway');
  }

  getFlywayDatatable(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="flyway-datatable"]');
  }

}
