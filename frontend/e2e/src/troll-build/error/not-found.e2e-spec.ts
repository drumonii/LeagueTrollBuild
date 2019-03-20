import { NotFoundPage } from './not-found.po';

describe('not found page', () => {
  const page = new NotFoundPage();

  beforeEach(() => {
    page.navigateTo();

    expect(page.getTitle()).toBe('League Troll Build');
  });

  it('should show not found (404) page', () => {
    expect(page.getMsgHeader().getText()).toBe('Page Not Found');

    expect(page.getMsgBody().getText())
      .toBe('The page you requested was not found. Please double check the URL and try again.');

    const returnToHomeLink = page.getReturnToHomeLink();
    expect(returnToHomeLink.getText()).toBe('Return to home');
    returnToHomeLink.getAttribute('href').then((link) => {
      expect(page.getHrefLink(link)).toBe('/');
    });
  });

});
