import { AdminBatchPage } from '../../pages/batch/admin-batch.po';

describe('admin batch page', () => {
  const page = new AdminBatchPage();

  describe('unauthenticated user', () => {

    beforeEach(() => {
      page.navigateTo();
    });

    it('should redirect', () => {
      // wait for page to redirect
      cy.wait(150).then(() => {
        page.getCurrentUrl().should('equal', page.getRedirectedUrl());
      });
    });

  });

  describe('authenticated admin', () => {

    beforeEach(() => {
      page.loginAdmin();
      page.navigateTo();

      // wait for page to redirect
      cy.wait(150).then(() => {
        page.getTitleContent().should('equal', 'Batch Jobs');
      });
    });

    it('should show the batch jobs datatable', () => {
      page.getBatchJobsDatatable().should('exist');

      cy.get('#clr-dg-row1 .datagrid-expandable-caret button.datagrid-expandable-caret-button').click();

      page.getStepExecutionsDetail().should('exist');
    });

  });

});
