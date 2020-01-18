import { AdminAppPage } from './admin-app.po';

describe('admin-app', () => {
  const page = new AdminAppPage();

  beforeEach(async () => {
    await page.navigateTo();
  });

  it('show show the admin header', async () => {
    expect(await page.getHeaderText()).toBe('League Troll Build Admin');
  });

  it('show show the admin footer', async () => {
    expect(await page.getFooter().isPresent()).toBeTruthy();
  });

});
