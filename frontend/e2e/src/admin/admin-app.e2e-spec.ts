import { AdminAppPage } from './admin-app.po';

describe('admin-app', () => {
  const page = new AdminAppPage();

  beforeEach(async () => {
    await page.navigateTo();
  });

  it('show show the admin header', () => {
    expect(page.getHeaderText()).toBe('League Troll Build Admin');
  });

  it('show show the admin footer', () => {
    expect(page.getFooter().isPresent()).toBe(true);
  });

});
