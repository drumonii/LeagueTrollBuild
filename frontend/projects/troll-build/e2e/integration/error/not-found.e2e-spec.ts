import { NotFoundPage } from '../../pages/error/not-found.po';

describe('not found page', () => {
  const page = new NotFoundPage();

  beforeEach(() => {
    page.navigateTo();

    page.getTitle().should('equal', 'League Troll Build');
  });

  it('should show not found (404) page', () => {
    page.getMsgHeader().should('have.text', 'Page Not Found');

    page.getMsgBody().should('have.text', 'The page you requested was not found. Please double check the URL and try again.');

    page.getReturnToHomeLink()
      .should((link) => {
        expect(link.text().trim()).to.equal('Return to home');
      })
      .and('have.attr', 'href')
      .and('include', '/')
  });

});
