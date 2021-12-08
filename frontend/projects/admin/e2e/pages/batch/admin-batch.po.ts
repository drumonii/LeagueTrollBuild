import { BaseAdminPage } from '../base-admin.po';

export class AdminBatchPage extends BaseAdminPage {

  navigateTo(): void {
    cy.visit('/admin/batch');
  }

  getBatchJobsDatatable(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="batch-jobs-datatable"]');
  }

  getStepExecutionsDetail(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('[data-e2e="step-executions-table"]');
  }

}
