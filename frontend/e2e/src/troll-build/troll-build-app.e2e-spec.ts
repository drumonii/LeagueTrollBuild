import { by, element } from 'protractor';

import { TrollBuildAppPage } from './troll-build-app.po';

describe('troll-build-app', () => {
  const page = new TrollBuildAppPage();

  beforeEach(async () => {
    await page.navigateTo();
  });

  afterEach(() => {
    expect(page.getTitle()).toBe('League Troll Build');

    // root should redirect to /champions
    expect(page.getCurrentUrl()).toBe('/champions');
  });

  it('should have body styles', async () => {
    const body = element(by.css('body'));
    const bodyBgImageCss = body.getCssValue('background-image');
    expect(bodyBgImageCss).toContain('background.jpg');
    const bodyBgRpeatCss = body.getCssValue('background-repeat');
    expect(bodyBgRpeatCss).toBe('no-repeat');

    const bodyBgColorCss = await element(by.css('body')).getCssValue('background-color');
    if (bodyBgColorCss.indexOf('rgba') === -1) {
      expect(bodyBgColorCss).toBe('rgb(0, 0, 0)');
    } else { // if IE
      expect(bodyBgColorCss).toBe('rgba(0, 0, 0, 1)');
    }
  });

  it('should show the troll build header', () => {
    expect(page.getHeaderText()).toBe('League Troll Build');
  });

  it('should show the troll build footer', () => {
    expect(page.getFooter().isPresent()).toBe(true);
  });

});
