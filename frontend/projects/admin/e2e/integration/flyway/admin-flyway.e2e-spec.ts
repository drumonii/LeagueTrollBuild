import { AdminFlywayPage } from '../../pages/flyway/admin-flyway.po';

describe('admin flyway page', () => {
  const page = new AdminFlywayPage();

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
        page.getTitleContent().should('equal', 'Flyway Migrations');
      });
    });

    it('should show the Flyway datatable', () => {
      page.getFlywayDatatable().should('exist');
    });

  });

});
