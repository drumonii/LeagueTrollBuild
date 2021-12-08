export abstract class BaseAdminPage {

  abstract navigateTo(): void;

  getTitle(): Cypress.Chainable<string> {
    return cy.title();
  }

  getTitleContent(): Cypress.Chainable<string> {
    return this.getTitle()
      .then((title) => title.substring(title.indexOf('|') + 2));
  }

  getCurrentUrl(): Cypress.Chainable<string> {
    return cy.url()
      .then((url) => url.substring(url.lastIndexOf('/')));
  }

  getRedirectedUrl(): string {
    return '/'; // will route to /champions with troll-build app running
  }

  getUsername(): string {
    return 'admin';
  }

  getPassword(): string {
    return 'password'; // see USERS.sql
  }

  getUsernameInput(): Cypress.Chainable<JQuery<HTMLInputElement>> {
    return cy.get('[data-e2e="username-input"]');
  }

  getPasswordInput(): Cypress.Chainable<JQuery<HTMLInputElement>> {
    return cy.get('[data-e2e="password-input"]');
  }

  loginAdmin(): void {
    const username = this.getUsername();
    const password = this.getPassword();

    cy.getCookie('XSRF-TOKEN').then((xsrfToken) => {
      cy.request({
        method: 'POST',
        url: '/api/admin/login',
        form: true, // indicates the body should be form urlencoded and sets Content-Type: application/x-www-form-urlencoded headers
        body: {
          username,
          password
        },
        headers: {
          'X-XSRF-TOKEN': xsrfToken.value
        }
      });
    });
  }

}
