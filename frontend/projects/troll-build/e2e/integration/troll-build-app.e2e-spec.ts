import { TrollBuildAppPage } from '../pages/troll-build-app.po';

describe('troll-build-app', () => {
  const page = new TrollBuildAppPage();

  beforeEach(() => {
    page.navigateTo();
  });

  afterEach(() => {
    page.getTitle().should('have.text', 'League Troll Build');

    // root should redirect to /champions
    page.getCurrentUrl().should('have.text', '/champions');
  });

  it('should have body styles', () => {
    cy.get('nb-layout .layout')
      .should('have.css', 'background-image', 'background.jpg')
      .and('background-repeat', 'no-repeat')
      .and('background-color', '#000000');
  });

  it('should show the troll build header', () => {
    page.getHeader().should('have.text', 'League Troll Build');
  });

  it('should show the troll build footer', () => {
    page.getFooter().should('exist')
    page.getLatestSavedVersion().should('contain.text', '.');
  });

});
