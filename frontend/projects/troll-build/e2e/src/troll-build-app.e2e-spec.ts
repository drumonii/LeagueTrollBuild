import { by, element } from 'protractor';

import { TrollBuildAppPage } from './troll-build-app.po';

describe('troll-build-app', () => {
  const page = new TrollBuildAppPage();

  beforeEach(async () => {
    await page.navigateTo();
  });

  afterEach(async () => {
    expect(await page.getTitle()).toBe('League Troll Build');

    // root should redirect to /champions
    expect(await page.getCurrentUrl()).toBe('/champions');
  });

  it('should have body styles', async () => {
    const body = element(by.css('nb-layout .layout'));
    const bodyBgImageCss = await body.getCssValue('background-image');
    expect(bodyBgImageCss).toContain('background.jpg');
    const bodyBgRpeatCss = await body.getCssValue('background-repeat');
    expect(bodyBgRpeatCss).toBe('no-repeat');

    const bodyBgColorCss = await element(by.css('body')).getCssValue('background-color');
    expect(bodyBgColorCss).toBe('rgba(0, 0, 0, 0)');
  });

  it('should show the troll build header', async () => {
    expect(await page.getHeaderText()).toBe('League Troll Build');
  });

  it('should show the troll build footer', async () => {
    expect(await page.getFooter().isPresent()).toBeTruthy();
    expect(await page.getLatestSavedVersion().getText()).toContain('.');
  });

});
