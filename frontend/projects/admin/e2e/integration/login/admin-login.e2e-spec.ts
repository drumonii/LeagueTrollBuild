import { AdminLoginPage } from '../../pages/login/admin-login.po';

describe('admin login page', () => {
  const page = new AdminLoginPage();

  beforeEach(() => {
    page.navigateTo();

    // wait for title to update
    cy.wait(150).then(() => {
      page.getTitleContent().should('equal', 'Admin Login');
    });
  });

  describe('not logged in', () => {

    it('should hide the authenticated header', () => {
      cy.get('.header-nav').should('not.exist');
      cy.get('.header-actions').should('not.exist');
    });

    it('should not display logged out alert', () => {
      page.getLoggedOutAlert().should('not.exist');
    });

  });

  describe('with empty username and password', () => {

    it('should not allow logins', () => {
      page.getLoginBtn().should('be.disabled');
    });

  });

  describe('with invalid credentials', () => {

    it('should not login the admin user', () => {
      page.getUsernameInput().type(page.getUsername());
      page.getPasswordInput().type('invalid password!');

      page.attemptLoginAdmin();
      page.getInvalidCredentialsAlert().should('exist');
    });

  });

  describe('with valid credentials', () => {

    it('should login and logout the admin user', () => {
      page.getUsernameInput().type(page.getUsername());
      page.getPasswordInput().type(page.getPassword());

      page.attemptLoginAdmin();
      cy.wait(150).then(() => {
        page.getCurrentUrl().should('equal', '/admin');
      });

      cy.get('.header-nav').should('exist');
      cy.get('.header-actions').should('exist');

      // ensure login page is not accessible when already logged in
      page.navigateTo();
      cy.wait(150).then(() => {
        page.getCurrentUrl().should('equal', '/admin');
      });

      page.attemptLogoutAdmin();
      page.getLoggedOutAlert().should('exist');
    });

  });

});
