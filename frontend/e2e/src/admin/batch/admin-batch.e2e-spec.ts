import { AdminBatchPage } from './admin-batch.po';

describe('admin batch page', () => {
  const page = new AdminBatchPage();

  describe('unauthenticated user', () => {

    beforeEach(() => {
      page.navigateTo();
    });

    it('should redirect', () => {
      expect(page.getCurrentUrl()).toBe(page.getRedirectedUrl());
    });

  });

  describe('authenticated admin', () => {

    beforeEach(() => {
      page.loginAdmin();
      page.navigateTo();

      expect(page.getTitleContent()).toBe('Batch Jobs');
    });

    afterEach(() => {
      page.logoutAdmin();
    });

    it('should show the Flyway datatable', () => {
      expect(page.getBatchJobsDatatable().isPresent()).toBe(true);
    });

  });

});
