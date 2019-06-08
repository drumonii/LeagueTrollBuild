import { AdminFlywayPage } from './admin-flyway.po';

describe('admin flyway page', () => {
  const page = new AdminFlywayPage();

  describe('unauthenticated user', () => {

    beforeEach(async () => {
      await page.navigateTo();
    });

    it('should redirect', async () => {
      expect(await page.getCurrentUrl()).toBe(page.getRedirectedUrl());
    });

  });

  describe('authenticated admin', () => {

    beforeEach(async () => {
      await page.loginAdmin();
      await page.navigateTo();

      expect(await page.getTitleContent()).toBe('Flyway Migrations');
    });

    afterEach(async () => {
      await page.logoutAdmin();
    });

    it('should show the Flyway datatable', async () => {
      expect(await page.getFlywayDatatable().isPresent()).toBe(true);
    });

  });

});
