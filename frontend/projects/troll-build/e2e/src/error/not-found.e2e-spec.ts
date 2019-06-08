import { NotFoundPage } from './not-found.po';

describe('not found page', () => {
  const page = new NotFoundPage();

  beforeEach(async () => {
    await page.navigateTo();

    expect(await page.getTitle()).toBe('League Troll Build');
  });

  it('should show not found (404) page', async () => {
    expect(await page.getMsgHeader().getText()).toBe('Page Not Found');

    expect(await page.getMsgBody().getText())
      .toBe('The page you requested was not found. Please double check the URL and try again.');

    const returnToHomeLink = page.getReturnToHomeLink();
    expect(await returnToHomeLink.getText()).toBe('Return to home');

    const returnToHomeHref = await returnToHomeLink.getAttribute('href');
    expect(page.getHrefLink(returnToHomeHref)).toBe('/');
  });

});
