import { by, element } from 'protractor';

import { TrollBuildAppPage } from './troll-build-app.po';

describe('troll-build-app', () => {
  const page = new TrollBuildAppPage();

  beforeEach(() => {
    page.navigateTo();
  });

  it('should have body styles', () => {
    const bodyCss = element(by.css('body')).getCssValue('background');
    expect(bodyCss).toContain('background.jpg');
    expect(bodyCss).toContain('rgb(0, 0, 0)');
  });

  it('should show the troll build header', () => {
    expect(page.getHeaderText()).toEqual('League Troll Build');
  });

  it('should show the troll build footer', () => {
    expect(page.getFooter().isPresent()).toBe(true);
  });

});
