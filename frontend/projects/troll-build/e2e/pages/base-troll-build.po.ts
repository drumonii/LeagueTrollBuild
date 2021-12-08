export abstract class BaseTrollBuildPage {

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

}
