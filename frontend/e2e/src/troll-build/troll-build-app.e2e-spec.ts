import { by, element } from 'protractor';

import { TrollBuildAppPage } from './troll-build-app.po';

describe('troll-build-app', () => {
  const page = new TrollBuildAppPage();

  beforeEach(() => {
    page.navigateTo();
  });

  it('should have body styles', () => {
    const body = element(by.css('body'));
    const bodyBgImageCss = body.getCssValue('background-image');
    expect(bodyBgImageCss).toContain('background.jpg');
    const bodyBgRpeatCss = body.getCssValue('background-repeat');
    expect(bodyBgRpeatCss).toBe('no-repeat');
    const bodyBgColorCss = body.getCssValue('background-color');
    expect(bodyBgColorCss).toBe('rgba(0, 0, 0, 1)');
  });

  it('should show the troll build header', () => {
    expect(page.getHeaderText()).toEqual('League Troll Build');
  });

  it('should show the troll build footer', () => {
    expect(page.getFooter().isPresent()).toBe(true);
  });

});
