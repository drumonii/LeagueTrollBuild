import { AdminBatchPage } from './admin-batch.po';

describe('admin batch page', () => {
  const page = new AdminBatchPage();

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

      expect(await page.getTitleContent()).toBe('Batch Jobs');
    });

    afterEach(async () => {
      await page.logoutAdmin();
    });

    it('should show the Flyway datatable', async () => {
      expect(await page.getBatchJobsDatatable().isPresent()).toBe(true);
    });

  });

});
